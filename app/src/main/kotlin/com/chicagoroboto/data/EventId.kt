package com.chicagoroboto.data

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
annotation class EventId