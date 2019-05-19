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

        container.register([UIViewController].self, name: "MainViewControllers") { r in
            let factory = r.resolve(SessionListViewControllerFactory.self)!
            let c1 = factory.create(date: "2019-04-25")
            c1.tabBarItem = UITabBarItem(title: "Sessions", image: UIImage.init(named: "ic_schedule_black_18pt"), tag: 0)

            let c2 = factory.create(date: "2019-04-26")
            c2.tabBarItem = UITabBarItem(title: "Sessions2", image: UIImage.init(named: "ic_favorite_border_black_18pt"), tag: 1)
            return [c1, c2]
        }

        container.register(HomeViewController.self) { r in
            return HomeViewController(viewControllers: r.resolve([UIViewController].self, name: "MainViewControllers")!)
        }
    }
}
