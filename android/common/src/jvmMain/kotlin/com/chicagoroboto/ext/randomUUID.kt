package com.chicagoroboto.ext

import java.util.UUID

actual fun randomUUID(): String = UUID.randomUUID().toString()