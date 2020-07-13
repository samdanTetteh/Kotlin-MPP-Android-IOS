package com.superawesome.multiplatform.Application

import android.app.Application
import com.superawesome.sharedcode.repository.TodoRepository
import com.superawesome.sharedcode.repository.appContext

class TodosApplication : Application() {

    val todosRepository by lazy { TodoRepository() }

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }
}