package com.superawesome.sharedcode

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

internal actual val applicationDispatcher: CoroutineContext = Dispatchers.IO