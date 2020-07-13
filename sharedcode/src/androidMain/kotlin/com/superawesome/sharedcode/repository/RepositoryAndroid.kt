package com.superawesome.sharedcode.repository

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.superawesome.multiplatform.db.Database

lateinit var appContext: Context
/**
 * Make sure database schema classes are generated
 * **/
internal actual fun cache(): Database {
    val driver = AndroidSqliteDriver(Database.Schema, appContext, "todo.db")
    return Database(driver)
}