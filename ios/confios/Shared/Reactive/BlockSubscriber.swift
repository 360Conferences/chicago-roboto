//
//  BlockSubscriber.swift
//  confios
//
//  Created by Ryan Harter on 6/12/19.
//  Copyright © 2019 Chicago Roboto. All rights reserved.
//

import Foundation
import common

extension Publisher {

    /**
        Subscribes to the Publisher, returning a [Disposable], which will automatically cancel the subscription on
        `deinit`.
    */
    func subscribe<T>(
            onNext onNextBlock: @escaping (T) -> Void,
            onError onErrorBlock: @escaping (Error) -> Void = {_ in},
            onComplete onCompleteBlock: @escaping () -> Void = {}
    ) -> Disposable {
        let subscriber =  BlockSubscriber<T>(
            onNextBlock: onNextBlock,
            onErrorBlock: {_ in },
            onCompleteBlock: {}
        )
        subscribe(s: subscriber)
        return subscriber
    }
}

protocol Disposable {
    func dispose()
}

class SubscriberError: Error {
    let message: String?
    let cause: SubscriberError?
    init(_ message: String?, _ cause: SubscriberError? = nil) {
        self.message = message
        self.cause = cause
    }
}

extension KotlinThrowable {
    func asSubscriberError() -> SubscriberError {
        return SubscriberError(self.message, self.cause?.asSubscriberError())
    }
}

class BlockSubscriber<T>: Subscriber, Disposable {

    private var subscription: Subscription?
    private var cancelled = false

    private let onNextBlock: (T) -> Void
    private let onErrorBlock: (Error) -> Void
    private let onCompleteBlock: () -> Void
    
    init(onNextBlock: @escaping (T) -> Void, onErrorBlock: @escaping (Error) -> Void, onCompleteBlock: @escaping () -> Void) {
        self.onNextBlock = onNextBlock
        self.onErrorBlock = onErrorBlock
        self.onCompleteBlock = onCompleteBlock
    }

    func onSubscribe(s: Subscription) {
        if subscription != nil {
            s.cancel()
        } else {
            subscription = s
            s.request(n: 1)
        }
    }

    func onNext(t: Any?) {
        guard !cancelled else { return }

        onNextBlock(t as! T)
        subscription?.request(n: 1)
    }

    func onError(t: KotlinThrowable) {
        onErrorBlock(t.asSubscriberError())
    }

    func onComplete() {
        onCompleteBlock()
    }

    func dispose() {
        if (!cancelled) {
            cancelled = true
            subscription?.cancel()
        }
    }

    deinit {
        dispose()
    }
}
