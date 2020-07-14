package com.superawesome.multiplatform.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.superawesome.sharedcode.api.AddDataException
import com.superawesome.sharedcode.model.Todo
import com.superawesome.sharedcode.repository.TodoRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

/**
 * View model class for repository actions and observable methods.
 * **/
class TodoViewModel(private val repository: TodoRepository) : ViewModel() {

    private val todoListMutable = MutableLiveData<List<Todo>>()
    val todos: LiveData<List<Todo>> = todoListMutable

    private val errorMutable = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> = errorMutable

    private val isRefreshingMutable = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean> = isRefreshingMutable

    init {
        loadMembers()
    }

    /**
     *  load data based on boolean parameter [fromRemote]
     * **/
    fun loadMembers(fromRemote: Boolean = false) {
        // Fetch Data with flow builder
        viewModelScope.launch {
            repository.fetchTodoFlowData(fromRemote)
                .onStart {
                    isRefreshingMutable.value = true
                }.onCompletion {
                    isRefreshingMutable.value = false
                }.catch {
                    errorMutable.value = it
                }.collect {
                    todoListMutable.value = it
                }
        }
    }

    /**
     *  creates new task and persists it in local repository
     * **/
    fun insertTask(title: String) {
        if(title.isNotEmpty()){
            val todo = Todo(title = title, completed = false)
            repository.cacheTodoData(todo)

            // Makes sure observing views get notified
            loadMembers(false)
        }else{
            errorMutable.value = AddDataException()
        }
    }
}