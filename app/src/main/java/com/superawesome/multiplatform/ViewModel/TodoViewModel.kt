package com.superawesome.multiplatform.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.superawesome.sharedcode.model.Todo
import com.superawesome.sharedcode.repository.TodoRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect


class TodoViewModel(private val repository: TodoRepository) : ViewModel() {

    private val _todos = MutableLiveData<List<Todo>>()
    val todos: LiveData<List<Todo>> = _todos

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> = _error

    private val _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    init {
        loadMembers()
    }


     //load data based on boolean parameter [fromCache]
     fun loadMembers(fromRemote: Boolean = false) {
         // Fetch Data with flow builder
        viewModelScope.launch {
            repository.fetchTodoFlowData(fromRemote)
                .onStart {
                    _isRefreshing.value = true
                }.onCompletion {
                    _isRefreshing.value = false
                }.catch {
                    _error.value = it
                }.collect{
                    _todos.value = it
                }
        }
    }


    //creates new task and persists it in local repository
    fun insertTask(title : String){
        val todo = Todo(title = title, completed = false)
        repository.cacheTodoData(todo)
        loadMembers(false)
    }
}