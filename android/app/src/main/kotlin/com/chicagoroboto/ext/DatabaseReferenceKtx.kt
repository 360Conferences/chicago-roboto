package com.chicagoroboto.ext

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

inline fun DatabaseReference.addValueEventListener(
    crossinline onDataChange: (snapshot: DataSnapshot) -> Unit,
    crossinline onCancelled: (error: DatabaseError) -> Unit = {}
): ValueEventListener = this.addValueEventListener(object : ValueEventListener {
  override fun onDataChange(snapshot: DataSnapshot) = onDataChange(snapshot)
  override fun onCancelled(error: DatabaseError) = onCancelled(error)
})
