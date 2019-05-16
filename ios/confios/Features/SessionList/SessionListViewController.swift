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

private let sessionReuseIdentifier = "SessionCell"

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

    required init?(coder aDecoder: NSCoder) {
        fatalError("die")
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        self.view.tintColor = .black
        self.view.backgroundColor = containerScheme.colorScheme.backgroundColor

        self.collectionView.backgroundColor = containerScheme.colorScheme.backgroundColor

        let sessionCellNib = UINib.init(nibName: "SessionCell", bundle: nil)
        collectionView.register(sessionCellNib, forCellWithReuseIdentifier: sessionReuseIdentifier)

        sessionProvider.addSessionListener(key: KEY, date: date, onComplete: sessionListener)
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)

        if self.collectionViewLayout is UICollectionViewFlowLayout {
            let flowLayout = self.collectionViewLayout as? UICollectionViewFlowLayout
            let HORIZONTAL_SPACING: CGFloat = 8.0
            let itemSize = CGSize(
                    width: self.view.frame.size.width - 2.0 * HORIZONTAL_SPACING,
                    height: 120.0
            )
            flowLayout?.itemSize = itemSize
        }
    }

    override func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return sessions.count
    }

    override func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: sessionReuseIdentifier, for: indexPath) as! SessionCell
        cell.bind(to: sessions[indexPath.item])
        return cell
    }
}
