//
//  HomeViewController.swift
//  confios
//
//  Created by Ryan Harter on 2019-05-15.
//  Copyright © 2019 Chicago Roboto. All rights reserved.
//

import UIKit
import MaterialComponents

class HomeViewController: UITabBarController {

    private static let indexNotFound = -1

    private let navigationBar = MDCBottomNavigationBar()

    init(viewControllers: [UIViewController]) {
        super.init(nibName: nil, bundle: nil)
        super.viewControllers = viewControllers
    }

    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("die")
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        view.backgroundColor = containerScheme.colorScheme.backgroundColor
        view.addSubview(navigationBar)
    }

}
