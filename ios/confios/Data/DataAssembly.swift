//
// Created by Ryan Harter on 2019-04-26.
// Copyright (c) 2019 Chicago Roboto. All rights reserved.
//

import Foundation
import Swinject
import FirebaseDatabase
import common

class DataAssembly: Assembly {

    private let eventId: String

    init(eventId: String) {
        self.eventId = eventId
    }

    func assemble(container: Container) {
        
        // root db ref
        container.register(FirebaseDatabaseReference.self) { r in
            Database.database().isPersistenceEnabled = true
            let eventRef = Database.database().reference()
                    .child("events")
                    .child(self.eventId)
            eventRef.keepSynced(true)
            return FirebaseDatabaseReference(eventRef)
        }

        container.register(SessionProvider.self) { r in
            SessionProviderImpl(db: r.resolve(FirebaseDatabaseReference.self)!)
        }
    }
}
