package com.chicagoroboto.data

import com.google.firebase.database.*
import java.util.*

class FirebaseSessionDateProvider(dbRoot: DatabaseReference) : SessionDateProvider {

    private val ref: DatabaseReference = dbRoot.child("config").child("event_dates")

    private val listeners: MutableMap<Any, ValueEventListener> = HashMap()

    override fun addSessionDateListener(key: Any, onComplete: (List<String>?) -> Unit) {
        val listener = object : ValueEventListener {
            override fun onDataChange(data: DataSnapshot) {
                val typeIndicator = object : GenericTypeIndicator<ArrayList<String>>() {}
                val dates = data.getValue(typeIndicator)
                onComplete(dates)
            }

            override fun onCancelled(error: DatabaseError) {
                onComplete(null)
            }
        }
        listeners[key] = listener
        ref.addValueEventListener(listener)
    }

    override fun removeSessionDateListener(key: Any) {
        listeners[key]?.let { ref.removeEventListener(it) }
    }
}