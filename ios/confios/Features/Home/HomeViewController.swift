//
//  HomeViewController.swift
//  confios
//
//  Created by Ryan Harter on 2019-05-15.
//  Copyright © 2019 Chicago Roboto. All rights reserved.
//

import UIKit

class HomeViewController: UITabBarController {

    init(viewControllers: [UIViewController]) {
        super.init(nibName: nil, bundle: nil)
        super.viewControllers = viewControllers

        navigationItem.title = "Chicago Roboto" // todo read from config
    }

    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("die")
    }
}
