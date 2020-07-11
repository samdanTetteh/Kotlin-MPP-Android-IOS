package com.superawesome.sharedcode.repository

import com.superawesome.multiplatform.db.Database
import com.superawesome.multiplatform.db.SuperawesomeQueries
import com.superawesome.sharedcode.api.RemoteDataException
import com.superawesome.sharedcode.api.TodoApi
import com.superawesome.sharedcode.applicationDispatcher
import com.superawesome.sharedcode.model.Todo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


internal expect fun cache(): Database

class TodoRepository(
    private val api: TodoApi,
    private val queries: SuperawesomeQueries = cache().superawesomeQueries
) {
    constructor() : this(api = TodoApi())

    /**
     * If [force] is set to true, attempt to load data from remote api.
     * If remote api is not available. throw [RemoteDataException]
     *
     * If [force] is set to false, attempt to load data from local cache.
     * If local cache is not available, propagate the exception encountered
     */
    fun fetchTodoFlowData(force: Boolean): Flow<List<Todo>> {
        return if (force) getTodoDataFromRemote() else getTodoDataFromCache()
    }

    fun fetchTodoData(
        onSuccess: (List<Todo>) -> Unit,
        onFailure: (Throwable) -> Unit,
        fromRemote: Boolean
    ) {
        GlobalScope.launch(applicationDispatcher) {
            fetchTodoFlowData(force = fromRemote)
                .catch { onFailure(it) }
                .collect { onSuccess(it) }
        }
    }

    private fun cacheTodoData(todos: List<Todo>) {
        queries.deleteAll()
        todos.forEach { todo ->
            queries.insert(
                todo.title,
                todo.completed
            )
        }
    }


    //insert a single task to local repository
    fun cacheTodoData(todo: Todo) {
        queries.insert(
          todo.title,
          todo.completed
        )
    }



    private fun getTodoDataFromRemote(): Flow<List<Todo>> {
        println("Getting todo data from remote")
        return flow {
            val todoData = api.getTodoData()
            cacheTodoData(todoData)
            emit(api.getTodoData())
        }.catch { error(RemoteDataException()) }
            .flowOn(applicationDispatcher)
    }

    private fun getTodoDataFromCache(): Flow<List<Todo>> {
        println("Getting todo data from cache")
        fun loadMembers() = queries.selectAll().executeAsList().
        map { Todo(id = it.id, title = it.title, completed = it.completed!!) }
        return flow { emit(loadMembers()) }
            .catch { error(RemoteDataException()) }
            .flowOn(applicationDispatcher)
    }
}


