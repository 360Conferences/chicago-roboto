package com.chicagoroboto.ext

import io.reactivex.Observable
import org.reactivestreams.Publisher

fun <T> Publisher<T>.asObservable(): Observable<T> = Observable.fromPublisher(this)