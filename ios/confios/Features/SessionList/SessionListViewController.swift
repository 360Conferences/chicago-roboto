//
//  SessionListViewController.swift
//  confios
//
//  Created by Ryan Harter on 4/30/19.
//  Copyright © 2019 Chicago Roboto. All rights reserved.
//

import UIKit
import common
import MaterialComponents.MaterialCards

private let KEY = "SESSION_LIST"

class SessionListViewControllerFactory {
    private let sessionProvider: SessionProvider
    init(sessionProvider: SessionProvider) {
        self.sessionProvider = sessionProvider
    }

    func create(date: String) -> SessionListViewController {
        return SessionListViewController(date: date, sessionProvider: sessionProvider)
    }
}

class SessionListViewController: UICollectionViewController {

    private let date: String
    private let sessionProvider: SessionProvider

    private var sessionListener: (([Session]?) -> KotlinUnit)!
    private var sessions: [Session] = [Session]() {
        didSet {
            self.collectionView.reloadData()
        }
    }

    init(date: String, sessionProvider: SessionProvider) {
        self.date = date
        self.sessionProvider = sessionProvider

        super.init(collectionViewLayout: UICollectionViewFlowLayout())

        self.sessionListener = { [weak self] (s: [Session]?) in
            if let sessions = s {
                self?.sessions = sessions
            }
            return KotlinUnit()
        }
    }

    required init?(coder aDecoder: NSCoder) { fatalError("die") }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        sessionProvider.addSessionListener(key: KEY, date: date, onComplete: sessionListener)
    }

    override func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return sessions.count
    }
    
    override func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "Cell", for: indexPath) as! SessionCell
        
        let session = sessions[indexPath.item]
        cell.title.text = session.title
        cell.room.text = session.location
        cell.speakers.text = session.speakers?.joined(separator: ", ")
        
        return cell
    }
}
