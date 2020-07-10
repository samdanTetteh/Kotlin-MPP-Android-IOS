package com.superawesome.multiplatform

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.superawesome.multiplatform.Application.TodosApplication
import com.superawesome.multiplatform.ViewModel.TodoViewModel
import com.superawesome.multiplatform.ui.TodoAdapter
import com.superawesome.sharedcode.api.RemoteDataException
import com.superawesome.sharedcode.model.Todo


class MainActivity : AppCompatActivity() {

    private val recyclerList by lazy {
        findViewById<RecyclerView>(R.id.todo_list)
    }


    private val repository by lazy {
        (application as TodosApplication).todosRepository
    }

    private val viewModel by lazy { TodoViewModel(repository) }


    lateinit var adapter : TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecyclerView()

        //Listen on data change events and show data from server is not data is found in cache
        viewModel.todos.observe(this, Observer {
            if (it.isEmpty()) {
                viewModel.loadMembers(true)
                Toast.makeText(this, "No data from remote", Toast.LENGTH_LONG).show()
            } else {
                showData(it)
                Toast.makeText(this, "No data from cache", Toast.LENGTH_LONG).show()
            }
        })

        viewModel.isRefreshing.observe(this, Observer {

        })

        // listen on error events from [viewModel] and show error if any
        viewModel.error.observe(this, Observer {
            showError(it)
        })

    }


    private fun showData(data: List<Todo>) {
        adapter.data = data
        adapter.notifyDataSetChanged()
    }


    private fun showError(error: Throwable) {
        val errorMessage = when (error) {
            is RemoteDataException -> getString(R.string.server_error)
            else -> getString(R.string.error_txt)
        }
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }


    private fun setupRecyclerView() {
        recyclerList.layoutManager = LinearLayoutManager(this)
        adapter = TodoAdapter(emptyList())
        recyclerList.adapter = adapter
    }
}