//
// Created by Ryan Harter on 2019-06-22.
// Copyright (c) 2019 Chicago Roboto. All rights reserved.
//

import UIKit
import common

@objc class SessionDatesViewController: UIViewController {

    @IBOutlet weak var tabs: UISegmentedControl!
    @IBOutlet weak var contentView: UIView!

    private let viewModel: SessionDateViewModel
    private let sessionListViewControllerFactory: SessionListViewControllerFactory

    private var disposables = [Disposable]()
    private var currentChildVC: UIViewController? = nil

    init(viewModel: SessionDateViewModel,
         sessionListViewControllerFactory: SessionListViewControllerFactory) {
        self.viewModel = viewModel
        self.sessionListViewControllerFactory = sessionListViewControllerFactory
        super.init(nibName: nil, bundle: nil)
    }

    required init?(coder aDecoder: NSCoder) { fatalError("die") }

    override func viewDidLoad() {
        super.viewDidLoad()

        tabs.addTarget(self, action: #selector(selectedDateChanged), for: .valueChanged)

        disposables <= self.viewModel.viewState.subscribe(
                onNext: { [weak self] (state: SessionDateViewState) in
                    self?.setDates(dates: state.dates)
                }
        )
    }

    @objc func selectedDateChanged(_ sender: Any) {
        let selectedDate: String?
        let selectedIndex = tabs.selectedSegmentIndex
        if selectedIndex != UISegmentedControl.noSegment {
            selectedDate = tabs.titleForSegment(at: selectedIndex)
        } else {
            selectedDate = tabs.titleForSegment(at: 0)
        }

        guard let date = selectedDate else { return }

        currentChildVC?.remove()
        currentChildVC = viewControllerForDate(date: date)
        if let child = currentChildVC {
            add(child, inContainer: contentView)
        }
    }

    private func viewControllerForDate(date: String) -> UIViewController {
        // todo cache viewcontrollers, probably
        return sessionListViewControllerFactory.create(date: date)
    }

    private func setDates(dates: [String]) {
        // todo diff the dates

        let selectedDate: String?
        let selectedIndex = tabs.selectedSegmentIndex
        if selectedIndex != UISegmentedControl.noSegment {
            selectedDate = tabs.titleForSegment(at: selectedIndex)
        } else {
            selectedDate = dates.first
        }

        tabs.removeAllSegments()
        dates.forEach { date in
            tabs.insertSegment(withTitle: date, at: tabs.numberOfSegments, animated: false)
        }

        let newIndex: Int
        if let previousDate = selectedDate,
           let index = dates.firstIndex(of: previousDate) {
            newIndex = index
        } else {
            newIndex = 0
        }
        tabs.selectedSegmentIndex = newIndex
    }
}

extension UIViewController {
    func add(_ child: UIViewController, inContainer container: UIView? = nil) {
        guard let view = container ?? self.view else { return }
        guard let newView = child.view else { return }
        addChild(child)
        view.addSubview(newView)
        child.didMove(toParent: self)
    }

    func remove() {
        guard parent != nil else {
            return
        }

        willMove(toParent: nil)
        view.removeFromSuperview()
        removeFromParent()
    }
}
