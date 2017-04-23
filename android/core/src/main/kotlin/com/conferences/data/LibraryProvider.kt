package com.conferences.data

import com.conferences.model.Library

interface LibraryProvider {
    fun libraries(onComplete: (libraries: List<Library>) -> Unit)
}