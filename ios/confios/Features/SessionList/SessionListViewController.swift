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
    private let viewModel: SessionListViewModel

    init(viewModel: SessionListViewModel) {
        self.viewModel = viewModel
    }

    func create(date: String) -> SessionListViewController {
        return SessionListViewController(viewModel: viewModel, date: date)
    }
}

class SessionListViewController: UICollectionViewController {

    private let date: String
    private let viewModel: SessionListViewModel

    private var disposables = [Disposable]()

    private var sessions: [SessionListViewState.Session] = [SessionListViewState.Session]() {
        didSet {
            self.collectionView.reloadData()
        }
    }

    init(viewModel: SessionListViewModel, date: String) {
        self.date = date
        self.viewModel = viewModel

        super.init(collectionViewLayout: UICollectionViewFlowLayout())
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

        disposables <= self.viewModel.viewState.subscribe(
                onNext: { [weak self] (state: SessionListViewState) in
                    self?.sessions = state.sessions
                },
                onError: { error in
                    NSLog("Failed to load state.", error.localizedDescription)
                },
                onComplete: {
                    NSLog("onComplete")
                }
        )

        self.viewModel.setDate(date: self.date)
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

    override func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        super.collectionView(collectionView, didSelectItemAt: indexPath)
    }
}
