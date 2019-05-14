//
// Created by Ryan Harter on 2019-04-30.
// Copyright (c) 2019 Chicago Roboto. All rights reserved.
//

import Foundation
import Swinject
import common

class AppAssembly: Assembly {
    func assemble(container: Container) {
        container.register(SessionListViewControllerFactory.self) { r in
            return SessionListViewControllerFactory(sessionProvider: r.resolve(SessionProvider.self)!)
        }
    }
}
