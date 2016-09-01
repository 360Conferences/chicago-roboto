package com.gdgchicagowest.windycitydevcon.data

import com.gdgchicagowest.windycitydevcon.model.Session
import com.google.firebase.database.*
import java.util.*

class FirebaseSessionProvider : SessionProvider {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    private val listeners: MutableMap<Any, ValueEventListener> = HashMap<Any, ValueEventListener>()

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
        val query = database.child("sessions").child(date).orderByChild("start_time")
        query.addValueEventListener(listener)
    }

    override fun removeSessionListener(key: Any) {
        database.child("sessions").removeEventListener(listeners[key])
    }
}