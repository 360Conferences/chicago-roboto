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

        container.register(Settings.self) { r in
            return PlatformSettings()
        }

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
            return SessionProviderImpl(db: r.resolve(FirebaseDatabaseReference.self)!)
        }
        container.register(SpeakerProvider.self) { r in
            return SpeakerProviderImpl(db: r.resolve(FirebaseDatabaseReference.self)!)
        }
        container.register(FavoriteProvider.self) { r in
            return LocalFavoriteProvider(settings: r.resolve(Settings.self)!)
        }
    }
}
