package com.chicagoroboto.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.atomic.AtomicLong

class FirebaseDatabaseReference(private val ref: DatabaseReference) : DatabaseReferenceWrapper {

  private val handles = AtomicLong()
  @ExperimentalUnsignedTypes
  private val listeners = mutableMapOf<ULong, ValueEventListener>()

  override fun setValue(value: Any?) {
    ref.setValue(value)
  }

  override fun child(path: String): DatabaseReferenceWrapper =
    FirebaseDatabaseReference(ref.child(path))

  @ExperimentalUnsignedTypes override fun addValueEventListener(
      listener: ValueEventListenerWrapper
  ): ULong {
    val id = handles.getAndIncrement().toULong()
    val l = listener.asValueEventListener()
    listeners[id] = l
    ref.addValueEventListener(l)
    return id
  }

  @ExperimentalUnsignedTypes override fun addSingleValueEventListener(
      listener: ValueEventListenerWrapper
  ): ULong {
    val id = handles.getAndIncrement().toULong()
    val l = listener.asValueEventListener()
    listeners[id] = l
    ref.addListenerForSingleValueEvent(l)
    return id
  }

  @ExperimentalUnsignedTypes override fun removeEventListener(handle: ULong) {
    listeners.remove(handle)?.also { ref.removeEventListener(it) }
  }

  private fun ValueEventListenerWrapper.asValueEventListener(): ValueEventListener =
    object : ValueEventListener {
      override fun onCancelled(error: DatabaseError?) {
        this@asValueEventListener.onCancelled(error?.toException() ?: Exception())
      }

      override fun onDataChange(snapshot: DataSnapshot?) {
        this@asValueEventListener.onDataChange(snapshot?.let { FirebaseDataSnapshot(it) })
      }
    }
}

class FirebaseDataSnapshot(private val snapshot: DataSnapshot) : DataSnapshotWrapper {
  override fun child(path: String): DataSnapshotWrapper = FirebaseDataSnapshot(snapshot.child(path))
  override fun hasChild(path: String): Boolean = snapshot.hasChild(path)
  override fun hasChildren(): Boolean = snapshot.hasChildren()
  override fun exists(): Boolean = snapshot.exists()
  override fun getValue(): Any? = snapshot.value
  override fun getChildrenCount(): Long = snapshot.childrenCount
  override fun getRef(): DatabaseReferenceWrapper = FirebaseDatabaseReference(snapshot.ref)
  override fun getChildren(): Iterable<DataSnapshotWrapper> =
    snapshot.children.map { FirebaseDataSnapshot(it) }
}