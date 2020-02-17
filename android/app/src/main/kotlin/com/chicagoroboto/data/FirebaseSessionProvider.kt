package com.chicagoroboto.data

import android.util.Log
import com.chicagoroboto.model.Session
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class FirebaseSessionProvider(private val database: DatabaseReference) : SessionProvider {

    private val listeners: MutableMap<Any, ValueEventListener> = mutableMapOf()
    private val queries: MutableMap<Any, Query> = mutableMapOf()

    override fun addSessionListener(id: String, onComplete: (Session?) -> Unit) {
        val listener = object : ValueEventListener {
            override fun onDataChange(data: DataSnapshot) {
                onComplete(data.getValue(Session::class.java))
            }

            override fun onCancelled(e: DatabaseError) {
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
            override fun onDataChange(data: DataSnapshot) {
                val typeIndicator = object : GenericTypeIndicator<HashMap<String, @JvmSuppressWildcards Session>>() {}
                val sessions = data.getValue(typeIndicator)
                        ?.map { it.value }
                        ?.filter { it.start_time?.startsWith(date) ?: false }
                        ?.sortedBy { it.startTime }
                onComplete(sessions)
            }

            override fun onCancelled(e: DatabaseError) {
                onComplete(null)
            }
        }
        listeners[key] = listener

        val query = database.child("sessions")
        queries[key] = query
        query.addValueEventListener(listener)
    }

    override fun removeSessionListener(key: Any) {
        queries.remove(key)?.let { query ->
            listeners.remove(key)?.let { query.removeEventListener(it) }
        }
    }
}