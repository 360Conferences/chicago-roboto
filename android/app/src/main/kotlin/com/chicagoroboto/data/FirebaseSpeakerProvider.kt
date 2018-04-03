package com.chicagoroboto.data

import com.chicagoroboto.model.Speaker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import java.util.HashMap

class FirebaseSpeakerProvider(private val database: DatabaseReference) : SpeakerProvider {

    private val queries: MutableMap<Any, Query> = mutableMapOf()
    private val listeners: MutableMap<Any, ValueEventListener> = mutableMapOf()

    override fun addSpeakerListener(key: Any, onComplete: (Map<String, Speaker>?) -> Unit) {
        if (queries[key] != null) {
            removeSpeakerListener(key)
        }

        val listener = object : ValueEventListener {
            override fun onDataChange(data: DataSnapshot?) {
                val typeIndicator = object : GenericTypeIndicator<HashMap<String, Speaker>>() {}
                onComplete(data?.getValue(typeIndicator))
            }

            override fun onCancelled(e: DatabaseError?) {
                onComplete(null)
            }
        }
        listeners[key] = listener

        val query = database.child("speakers")
        query.addValueEventListener(listener)
        queries[key] = query
    }

    override fun addSpeakerListener(id: String, onComplete: (Speaker?) -> Unit) {
        if (queries[id] != null) {
            removeSpeakerListener(id)
        }

        val listener = object : ValueEventListener {
            override fun onDataChange(data: DataSnapshot?) {
                onComplete(data?.getValue(Speaker::class.java))
            }

            override fun onCancelled(e: DatabaseError?) {
                onComplete(null)
            }
        }
        listeners[id] = listener

        val query = database.child("speakers").child(id)
        query.addValueEventListener(listener)
        queries[id] = query
    }

    override fun removeSpeakerListener(key: Any) {
        val query = queries[key]
        query?.removeEventListener(listeners[key])

        queries.remove(key)
        listeners.remove(key)
    }
}