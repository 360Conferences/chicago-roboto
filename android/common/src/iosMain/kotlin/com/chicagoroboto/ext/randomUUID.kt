package com.chicagoroboto.ext

import platform.Foundation.NSUUID

actual fun randomUUID(): String = NSUUID().UUIDString