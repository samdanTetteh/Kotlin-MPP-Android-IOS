package com.superawesome.sharedcode.repository

import com.superawesome.multiplatform.db.Database
import com.superawesome.multiplatform.db.SuperawesomeQueries
import com.superawesome.sharedcode.api.RemoteDataException
import com.superawesome.sharedcode.api.TodosApi
import com.superawesome.sharedcode.applicationDispatcher
import com.superawesome.sharedcode.model.Todo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


internal expect fun cache(): Database

class TodoRepository(
    private val api: TodosApi,
    private val queries: SuperawesomeQueries = cache().superawesomeQueries
) {
    constructor() : this(api = TodosApi())

    /**
     * If [force] is set to true, attempt to load data from remote api.
     * If remote api is not available. throw [RemoteDataException]
     *
     * If [force] is set to false, attempt to load data from local cache.
     * If local cache is not available, propagate the exception encountered
     */
    fun fetchMembersAsFlow(force: Boolean): Flow<List<Todo>> {
        return if (force) getMembersFromRemote() else getMembersFromCache()
    }

    fun fetchMembers(
        onSuccess: (List<Todo>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        GlobalScope.launch(applicationDispatcher) {
            fetchMembersAsFlow(force = true)
                .catch { onFailure(it) }
                .collect { onSuccess(it) }
        }
    }

    private fun cacheMembers(todos: List<Todo>) {
        queries.deleteAll()
        todos.forEach { todo ->
            queries.insert(
                todo.title,
                todo.completed
            )
        }
    }

    private fun getMembersFromRemote(): Flow<List<Todo>> {
        println("Getting todos from remote")
        return flow {
            val todos = api.getTodos()
            cacheMembers(todos)
            emit(api.getTodos())
        }
            .catch { error(RemoteDataException()) }
            .flowOn(applicationDispatcher)
    }

    private fun getMembersFromCache(): Flow<List<Todo>> {
        println("Getting todos from cache")
        fun loadMembers() = queries.selectAll().executeAsList().
        map { Todo(id = it.id, title = it.title, completed = it.completed!!) }
        return flow { emit(loadMembers()) }
            .catch { error(RemoteDataException()) }
            .flowOn(applicationDispatcher)
    }
}


