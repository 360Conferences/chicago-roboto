//
// Created by Ryan Harter on 2019-04-26.
// Copyright (c) 2019 Chicago Roboto. All rights reserved.
//

import Foundation
import FirebaseDatabase
import common

class FirebaseDatabaseReference: DatabaseReferenceWrapper {

    private let ref: FirebaseDatabase.DatabaseReference
    private let handles = [String:UInt]()

    init(_ ref: FirebaseDatabase.DatabaseReference) {
        self.ref = ref
    }

    func child(path: String) -> DatabaseReferenceWrapper {
        return FirebaseDatabaseReference(ref.child(path))
    }

    func addValueEventListener(listener: ValueEventListenerWrapper) -> UInt64 {
        let handle = ref.observe(DataEventType.value, with: { (snapshot) in
            listener.onDataChange(data: FirebaseDataSnapshotWrapper(snapshot))
        }, withCancel: { error in
            listener.onCancelled(error: error as! KotlinException)
        })
        return UInt64(handle)
    }

    func removeEventListener(handle: UInt64) {
        ref.removeObserver(withHandle: UInt(handle))
    }
}

class FirebaseDataSnapshotWrapper: DataSnapshotWrapper {

    private let snapshot: DataSnapshot

    init(_ snapshot: DataSnapshot) {
        self.snapshot = snapshot
    }

    func child(path: String) -> DataSnapshotWrapper {
        return FirebaseDataSnapshotWrapper(snapshot.childSnapshot(forPath: path))
    }

    func hasChild(path: String) -> Bool {
        return snapshot.hasChild(path)
    }

    func hasChildren() -> Bool {
        return snapshot.hasChildren()
    }

    func exists() -> Bool {
        return snapshot.exists()
    }

    func getValue() -> Any {
        return snapshot.value
    }

    func getChildrenCount() -> Int64 {
        return Int64(snapshot.childrenCount)
    }

    func getRef() -> DatabaseReferenceWrapper {
        return FirebaseDatabaseReference(snapshot.ref)
    }

    func getChildren() -> Any {
        return snapshot.children
    }
}
