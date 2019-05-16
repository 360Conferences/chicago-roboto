//
// Created by Ryan Harter on 2019-05-15.
// Copyright (c) 2019 Chicago Roboto. All rights reserved.
//

import Foundation
import MaterialComponents.MaterialContainerScheme

func globalContainerScheme() -> MDCContainerScheming {
    let containerScheme = MDCContainerScheme()

    containerScheme.colorScheme.primaryColor = UIColor(red: 0.298, green: 0.384, blue: 0.424, alpha: 1.0)
    containerScheme.colorScheme.backgroundColor = UIColor(red: 248.0/255.0, green: 248.0/255.0, blue: 248.0/255.0, alpha: 1.0)
    containerScheme.colorScheme.surfaceColor = .white

    return containerScheme
}

// You can now access your global theme throughout your app:
let containerScheme = globalContainerScheme()