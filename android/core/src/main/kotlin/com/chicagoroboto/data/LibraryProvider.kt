package com.chicagoroboto.data

import com.chicagoroboto.model.Library

interface LibraryProvider {
    fun libraries(onComplete: (libraries: List<Library>) -> Unit)
}