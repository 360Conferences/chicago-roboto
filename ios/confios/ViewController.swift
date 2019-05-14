//
//  ViewController.swift
//  confios
//
//  Created by Ryan Harter on 2019-04-25.
//  Copyright © 2019 Chicago Roboto. All rights reserved.
//

import UIKit
import common
import Firebase

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()

        let sessionListFactory = AppDelegate.shared.resolver.resolve(SessionListViewControllerFactory.self)!
        let sessionListViewController = sessionListFactory.create(date: "2019-04-25")

        navigationController
    }

}

