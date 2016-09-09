package com.gdgchicagowest.windycitydevcon.data

import com.gdgchicagowest.windycitydevcon.model.Library

interface LibraryProvider {
    fun libraries(onComplete: (libraries: List<Library>) -> Unit)
}