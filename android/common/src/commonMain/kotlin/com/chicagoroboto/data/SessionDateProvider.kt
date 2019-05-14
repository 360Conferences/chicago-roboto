package com.chicagoroboto.data

interface SessionDateProvider {
  fun addSessionDateListener(key: Any, onComplete: (sessionDates: List<String>?) -> Unit)
  fun removeSessionDateListener(key: Any)

  @ExperimentalUnsignedTypes
  class Impl(dbRoot: DatabaseReferenceWrapper) : SessionDateProvider {

    private val ref = dbRoot.child("config").child("event_dates")

    private val handles = mutableMapOf<Any, ULong>()

    override fun addSessionDateListener(key: Any, onComplete: (List<String>?) -> Unit) {
      val listener = object : ValueEventListenerWrapper {
        override fun onDataChange(data: DataSnapshotWrapper?) {
          onComplete(data?.getList { it.getValue() as? String }?.filterNotNull())
        }

        override fun onCancelled(error: Exception) {
          onComplete(null)
        }
      }
      handles[key] = ref.addValueEventListener(listener)
    }

    override fun removeSessionDateListener(key: Any) {
      handles[key]?.also { ref.removeEventListener(it) }
    }
  }
}