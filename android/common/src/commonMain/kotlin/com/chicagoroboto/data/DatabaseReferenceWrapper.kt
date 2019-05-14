package com.chicagoroboto.data

interface DatabaseReferenceWrapper {

  fun setValue(value: Any)

  fun child(path: String): DatabaseReferenceWrapper
  @ExperimentalUnsignedTypes fun addValueEventListener(listener: ValueEventListenerWrapper): ULong

  @ExperimentalUnsignedTypes fun addSingleValueEventListener(
      listener: ValueEventListenerWrapper
  ): ULong

  @ExperimentalUnsignedTypes fun removeEventListener(handle: ULong)

  operator fun get(path: String) = child(path)
}

interface ValueEventListenerWrapper {
  fun onDataChange(data: DataSnapshotWrapper?)
  fun onCancelled(error: Exception)
}

interface DataSnapshotWrapper {
  fun child(path: String): DataSnapshotWrapper
  fun hasChild(path: String): Boolean
  fun hasChildren(): Boolean
  fun exists(): Boolean
  fun getValue(): Any?
  fun getChildrenCount(): Long
  fun getRef(): DatabaseReferenceWrapper
  fun getChildren(): Iterable<DataSnapshotWrapper>

  fun <T> getList(mapper: (DataSnapshotWrapper) -> T) = getChildren()
      .filter { it.exists() }
      .map(mapper)

  fun <T> map(transform: (DataSnapshotWrapper) -> T): T = transform(this)

  operator fun get(path: String) = child(path).getValue()
}

open class GenericTypeIndicatorWrapper<T>