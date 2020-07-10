package com.superawesome.sharedcode.repository

import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import com.superawesome.multiplatform.db.Database

internal actual fun cache(): Database {
    val driver = NativeSqliteDriver(Database.Schema, "todo.db")
    return Database(driver)
}