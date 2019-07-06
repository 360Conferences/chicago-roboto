//
// Created by Ryan Harter on 2019-06-22.
// Copyright (c) 2019 Chicago Roboto. All rights reserved.
//

import UIKit
import common

class SessionDatesViewController: UIViewController {

    @IBOutlet weak var toolbar: UIToolbar!
    @IBOutlet weak var tabs: UISegmentedControl!
    @IBOutlet weak var contentView: UIView!

    private let viewModel: SessionDateViewModel
    private let sessionListViewControllerFactory: SessionListViewControllerFactory

    private let dateFormatter = DateFormatter()
    private let titleFormatter = DateFormatter()
    private var disposables = [Disposable]()
    private var currentTabIndex = 0
    private var currentChildVC: UIViewController? = nil

    private var dates: [String]? = nil {
        didSet {
            // todo diff the dates
            tabs.removeAllSegments()

            guard let dates = self.dates else { return }
            let titles = dates
                    .map { dateFormatter.date(from: $0)! }
                    .map { titleFormatter.string(from: $0) }

            titles.forEach { date in
                tabs.insertSegment(withTitle: date, at: tabs.numberOfSegments, animated: false)
            }

            if tabs.selectedSegmentIndex == UISegmentedControl.noSegment {
                tabs.selectedSegmentIndex = 0
            }
            selectedDateChanged(self)
        }
    }

    init(viewModel: SessionDateViewModel,
         sessionListViewControllerFactory: SessionListViewControllerFactory) {
        self.viewModel = viewModel
        self.sessionListViewControllerFactory = sessionListViewControllerFactory
        super.init(nibName: nil, bundle: nil)

        dateFormatter.dateFormat = "YYYY-MM-dd"
        titleFormatter.dateFormat = "MMM dd"
    }

    required init?(coder aDecoder: NSCoder) { fatalError("die") }

    override func viewDidLoad() {
        super.viewDidLoad()
        toolbar.delegate = self

        tabs.addTarget(self, action: #selector(selectedDateChanged), for: .valueChanged)

        disposables <= self.viewModel.viewState.subscribe(
                onNext: { [weak self] (state: SessionDateViewState) in
                    self?.dates = state.dates
                }
        )
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        navigationController?.navigationBar.hideBorderLine()
    }

    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        navigationController?.navigationBar.showBorderLine()
    }

    @objc func selectedDateChanged(_ sender: Any) {
        guard let dates = self.dates else { return }
        let selectedDate: String
        let selectedIndex = tabs.selectedSegmentIndex
        if selectedIndex != UISegmentedControl.noSegment {
            selectedDate = dates[selectedIndex]
        } else {
            selectedDate = dates[0]
        }

        let newVC = viewControllerForDate(date: selectedDate)
        newVC.willMove(toParent: self)
        guard let oldVC = currentChildVC else {
            addChild(newVC)
            contentView.addSubview(newVC.view)
            newVC.didMove(toParent: self)
            currentChildVC = newVC
            currentTabIndex = selectedIndex
            return
        }
        
        oldVC.willMove(toParent: nil)
        addChild(newVC)
        
        let endFrame: CGRect
        let originalFrame = oldVC.view.frame
        if selectedIndex < currentTabIndex {
            // coming in from the left
            newVC.view.frame = originalFrame.applying(CGAffineTransform(translationX: -originalFrame.width, y: 0))
            endFrame = originalFrame.applying(CGAffineTransform(translationX: originalFrame.width, y: 0))
        } else {
            // coming in from the right
            newVC.view.frame = originalFrame.applying(CGAffineTransform(translationX: originalFrame.width, y: 0))
            endFrame = originalFrame.applying(CGAffineTransform(translationX: -originalFrame.width, y: 0))
        }
        
        transition(
            from: oldVC,
            to: newVC,
            duration: 0.1,
            options: [],
            animations: {
                newVC.view.frame = oldVC.view.frame
                oldVC.view.frame = endFrame
            },
            completion: { (finished) in
                oldVC.removeFromParent()
                newVC.didMove(toParent: self)
            }
        )
        
        currentChildVC = newVC
        currentTabIndex = selectedIndex
    }

    private func viewControllerForDate(date: String) -> UIViewController {
        // todo cache viewcontrollers, probably
        return sessionListViewControllerFactory.create(date: date)
    }
}

extension SessionDatesViewController: UIToolbarDelegate {
    public func position(for bar: UIBarPositioning) -> UIBarPosition {
        return .topAttached
    }
}

extension UINavigationBar {
    func showBorderLine() {
        findBorderLine().isHidden = false
    }

    func hideBorderLine() {
        findBorderLine().isHidden = true
    }

    private func findBorderLine() -> UIImageView! {
        return self.subviews
                .flatMap { $0.subviews }
                .compactMap { $0 as? UIImageView }
                .filter { $0.bounds.size.width == self.bounds.size.width }
                .filter { $0.bounds.size.height <= 2 }
                .first
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
