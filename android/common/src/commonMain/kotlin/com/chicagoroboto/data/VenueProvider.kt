package com.chicagoroboto.data

import com.chicagoroboto.model.Venue

interface VenueProvider {
    fun addVenueListener(key: Any, onComplete: (venue: Venue?) -> Unit)
    fun removeVenueListener(key: Any)

    @ExperimentalUnsignedTypes
    class Impl(private val db: DatabaseReferenceWrapper) : VenueProvider {

        private val handles = mutableMapOf<Any, ULong>()
        private val queries = mutableMapOf<Any, DatabaseReferenceWrapper>()

        private data class VenueValueEventListener(
            val key: Any, val onComplete: (venue: Venue?) -> Unit
        ) : ValueEventListenerWrapper {
            override fun onDataChange(data: DataSnapshotWrapper?) {
                onComplete(data?.map(Venue.Factory::fromDataSnapshot))
            }

            override fun onCancelled(error: Exception) {
                onComplete(null)
            }
        }

        override fun addVenueListener(key: Any, onComplete: (Venue?) -> Unit) {
            val listener = VenueValueEventListener(key, onComplete)
            val query = db.child("venue")
            queries[key] = query
            handles[key] = query.addValueEventListener(listener)
        }

        override fun removeVenueListener(key: Any) {
            val query = queries[key]
            val handle = handles[key]
            if (query != null && handle != null) {
                query.removeEventListener(handle)
            }

            queries.remove(key)
            handles.remove(key)
        }
    }
}