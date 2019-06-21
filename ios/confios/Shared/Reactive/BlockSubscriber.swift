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
    func subscribe<T>(
            onNext onNextBlock: @escaping (T) -> Void,
            onError onErrorBlock: @escaping (Error) -> Void = {_ in},
            onComplete onCompleteBlock: @escaping () -> Void = {}
    ) {
        self.subscribe(s: BlockSubscriber<T>(
            onNextBlock: onNextBlock,
            onErrorBlock: {_ in },
            onCompleteBlock: {}
        ))
    }
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

class BlockSubscriber<T>: Subscriber {
    private let onNextBlock: (T) -> Void
    private let onErrorBlock: (Error) -> Void
    private let onCompleteBlock: () -> Void
    
    init(onNextBlock: @escaping (T) -> Void, onErrorBlock: @escaping (Error) -> Void, onCompleteBlock: @escaping () -> Void) {
        self.onNextBlock = onNextBlock
        self.onErrorBlock = onErrorBlock
        self.onCompleteBlock = onCompleteBlock
    }

    func onSubscribe(s: Subscription) {

    }

    func onNext(t: Any?) {
        onNextBlock(t as! T)
    }

    func onError(t: KotlinThrowable) {
        onErrorBlock(t.asSubscriberError())
    }

    func onComplete() {
        onCompleteBlock()
    }
}
