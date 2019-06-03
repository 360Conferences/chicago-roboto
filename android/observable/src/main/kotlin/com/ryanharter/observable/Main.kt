package com.ryanharter.observable

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual fun mainDispatcher(): CoroutineDispatcher = Dispatchers.Main