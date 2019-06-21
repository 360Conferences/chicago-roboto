//
//  SessionCell.swift
//  confios
//
//  Created by Ryan Harter on 5/1/19.
//  Copyright © 2019 Chicago Roboto. All rights reserved.
//

import UIKit
import MaterialComponents
import common

class SessionCell: MDCCardCollectionCell {
    
    @IBOutlet weak var title: UILabel!
    @IBOutlet weak var speakers: UILabel!
    @IBOutlet weak var room: UILabel!
    @IBOutlet weak var favorite: UIImageView!

    override func awakeFromNib() {
        super.awakeFromNib()

        self.applyTheme(withScheme: containerScheme)
    }
    
    override func prepareForReuse() {
        clear()
    }
    
    func clear() {
        title.text = nil
        speakers.text = nil
        room.text = nil
    }
    
    func bind(to session: SessionListViewState.Session) {
        title.text = session.title
        speakers.text = session.speakers.joined(separator: ", ")
        room.text = session.room
        if (session.isFavorite) {
            favorite.image = UIImage(named: "ic_favorite_black_18pt")
        } else {
            favorite.image = UIImage(named: "ic_favorite_border_black_18pt")
        }
    }
}
