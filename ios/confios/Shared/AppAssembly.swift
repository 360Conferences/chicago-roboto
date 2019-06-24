//
// Created by Ryan Harter on 2019-04-30.
// Copyright (c) 2019 Chicago Roboto. All rights reserved.
//

import Foundation
import Swinject
import common

class AppAssembly: Assembly {
    func assemble(container: Container) {

        container.register(SessionDateViewModel.self) { r in
            SessionDateViewModel(sessionDateProvider: r.resolve(SessionDateProvider.self)!)
        }

        container.register(SessionDatesViewController.self) { r in
            SessionDatesViewController(
                    viewModel: r.resolve(SessionDateViewModel.self)!,
                    sessionListViewControllerFactory: r.resolve(SessionListViewControllerFactory.self)!
            )
        }

        container.register(SessionListViewModel.self) { r in
            SessionListViewModel(
                    sessionProvider: r.resolve(SessionProvider.self)!,
                    speakerProvider: r.resolve(SpeakerProvider.self)!,
                    favoriteProvider: r.resolve(FavoriteProvider.self)!
            )
        }

        container.register(SessionListViewControllerFactory.self) { r in
            SessionListViewControllerFactory(viewModel: r.resolve(SessionListViewModel.self)!)
        }

        container.register(SpeakerListViewController.self) { _ in SpeakerListViewController() }
        container.register(VenueViewController.self) { _ in VenueViewController() }

        container.register([UIViewController].self, name: "MainViewControllers") { r in
            let c1 = r.resolve(SessionDatesViewController.self)!
            c1.tabBarItem = UITabBarItem(title: "Sessions", image: UIImage.init(named: "ic_schedule_black_18pt"), tag: 0)

            let c2 = r.resolve(SpeakerListViewController.self)!
            c2.tabBarItem = UITabBarItem(title: "Speakers", image: UIImage.init(named: "ic_speakers_black_18pt"), tag: 1)

            let c3 = r.resolve(VenueViewController.self)!
            c3.tabBarItem = UITabBarItem(title: "Venue", image: UIImage.init(named: "ic_venue_black_18pt"), tag: 2)

            return [c1, c2, c3]
        }

        container.register(HomeViewController.self) { r in
            return HomeViewController(viewControllers: r.resolve([UIViewController].self, name: "MainViewControllers")!)
        }
    }
}
