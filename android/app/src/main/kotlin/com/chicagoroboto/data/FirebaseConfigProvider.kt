package com.chicagoroboto.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class FirebaseConfigProvider(db: DatabaseReference) : ConfigProvider {

    private val ref = db.child("config")

    override fun getTimezoneName(callback: (String) -> Unit) {
        val listener = object : ValueEventListener {
            override fun onDataChange(data: DataSnapshot?) {
                callback(data!!.getValue(String::class.java)!!)
            }

            override fun onCancelled(error: DatabaseError?) {
                // oh noes
            }
        }
        val query = ref.child("event-timezone")
        query.addListenerForSingleValueEvent(listener)
    }

}