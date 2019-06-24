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

        container.register(SessionDateProvider.self) { r in
            SessionDateProviderImpl(db: r.resolve(FirebaseDatabaseReference.self)!)
        }
        container.register(SessionProvider.self) { r in
            SessionProviderImpl(db: r.resolve(FirebaseDatabaseReference.self)!)
        }
        container.register(SpeakerProvider.self) { r in
            SpeakerProviderImpl(db: r.resolve(FirebaseDatabaseReference.self)!)
        }
        container.register(FavoriteProvider.self) { r in
            LocalFavoriteProvider(settings: r.resolve(Settings.self)!)
        }
    }
}
