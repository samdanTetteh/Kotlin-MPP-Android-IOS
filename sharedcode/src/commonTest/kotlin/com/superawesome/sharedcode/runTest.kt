package com.superawesome.sharedcode

expect fun <T> runTest(block: suspend () -> T)