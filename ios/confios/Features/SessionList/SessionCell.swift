//
//  SessionCell.swift
//  confios
//
//  Created by Ryan Harter on 5/1/19.
//  Copyright © 2019 Chicago Roboto. All rights reserved.
//

import UIKit
import MaterialComponents

class SessionCell: MDCCardCollectionCell {
    
    @IBOutlet weak var title: UILabel!
    @IBOutlet weak var speakers: UILabel!
    @IBOutlet weak var room: UILabel!
    @IBOutlet weak var favorite: UIImageView!

    override func awakeFromNib() {
        super.awakeFromNib()
        
        self.backgroundColor = .white
    }

}
