package com.superawesome.sharedcode

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * Coroutine context for concurrency
 * **/
internal actual val applicationDispatcher: CoroutineContext = Dispatchers.IO