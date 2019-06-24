//
// Created by Ryan Harter on 2019-06-24.
// Copyright (c) 2019 Chicago Roboto. All rights reserved.
//

import Foundation

extension Array {
    @inlinable public static func <= (lhs: inout [Element], rhs: Element) {
        lhs.append(rhs)
    }
}