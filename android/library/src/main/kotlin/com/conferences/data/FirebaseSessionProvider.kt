package com.conferences.data

import com.conferences.model.Session
import com.google.firebase.database.*
import java.util.*

class FirebaseSessionProvider(private val database: DatabaseReference) : SessionProvider {

    private val listeners: MutableMap<Any, ValueEventListener> = mutableMapOf()
    private val queries: MutableMap<Any, Query> = mutableMapOf()

    override fun addSessionListener(id: String, onComplete: (Session?) -> Unit) {
        val listener = object : ValueEventListener {
            override fun onDataChange(data: DataSnapshot?) {
                onComplete(data?.getValue(Session::class.java))
            }

            override fun onCancelled(e: DatabaseError?) {
                onComplete(null)
            }
        }
        listeners[id] = listener

        val query = database.child("sessions").child(id)
        queries[id] = query
        query.addValueEventListener(listener)
    }

    override fun addSessionListener(key: Any, date: String, onComplete: (List<Session>?) -> Unit) {
        val listener = object : ValueEventListener {
            override fun onDataChange(data: DataSnapshot?) {
                val typeIndicator = object : GenericTypeIndicator<ArrayList<Session>>() {}
                onComplete(data?.getValue(typeIndicator))
            }

            override fun onCancelled(p0: DatabaseError?) {
                onComplete(null)
            }
        }
        listeners[key] = listener

        val query = database.child("sessions_by_date").child(date).orderByChild("start_time")
        queries[key] = query
        query.addValueEventListener(listener)
    }

    override fun removeSessionListener(key: Any) {
        val query = queries.remove(key)
        query?.removeEventListener(listeners.remove(key))
    }
}