package com.chicagoroboto.ext

inline infix fun <T> T?.guard(block: () -> Nothing): T = this ?: block()
