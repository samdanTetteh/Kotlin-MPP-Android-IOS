package com.superawesome.multiplatform


import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.superawesome.multiplatform.Application.TodosApplication
import com.superawesome.multiplatform.ui.TodoAdapter
import com.superawesome.multiplatform.viewModel.TodoViewModel
import com.superawesome.sharedcode.api.AddDataException
import com.superawesome.sharedcode.api.RemoteDataException
import com.superawesome.sharedcode.model.Todo

/**
 * App main activity
 * **/
class MainActivity : AppCompatActivity() {

    lateinit var fab: FloatingActionButton
    lateinit var linearLayoutManager: LinearLayoutManager

    private val recyclerList by lazy {
        findViewById<RecyclerView>(R.id.todo_list)
    }

    private val holderView by lazy {
        findViewById<CoordinatorLayout>(R.id.holder_view)
    }

    private val repository by lazy {
        (application as TodosApplication).todosRepository
    }

    private val viewModel by lazy { TodoViewModel(repository) }

    lateinit var adapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab = findViewById(R.id.floating_action_button)

        setupRecyclerView()

        obverseViewModel()

        setPageEvents()
    }

    // Make sure view is always updated on data changes
    private fun obverseViewModel() {
        //Listen on data change events and show data from server if no data is found in cache once its is emitted.
        viewModel.todos.observe(this, Observer {
            if (it.isEmpty()) {
                viewModel.loadMembers(true)
            } else {
                showData(it)
            }
        })

        //Notify user when state is refreshing
        viewModel.isRefreshing.observe(this, Observer {
            Snackbar.make(holderView, getString(R.string.loading_txt), Snackbar.LENGTH_SHORT).show()
        })

        // listen on error events from [viewModel] and show error if any
        viewModel.error.observe(this, Observer {
            showError(it)
        })
    }


    private fun setPageEvents() {
        fab.setOnClickListener {
            showAddTaskDialog()
        }
    }

    // Set new [data] to Adapter and notify it any changes in data
    private fun showData(data: List<Todo>) {
        adapter.data = data
        adapter.notifyDataSetChanged()
    }

    //Display toast to show server error or any other error
    private fun showError(error: Throwable) {
        val errorMessage = when (error) {
            is RemoteDataException -> getString(R.string.server_error)
            is AddDataException -> getString(R.string.enter_task_title)
            else -> getString(R.string.error_txt)
        }
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }

    //Setup recycler view
    private fun setupRecyclerView() {
        linearLayoutManager = LinearLayoutManager(this)
        recyclerList.layoutManager = linearLayoutManager
        adapter = TodoAdapter(emptyList())
        recyclerList.adapter = adapter
    }

    //Display add task dialog
    private fun showAddTaskDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.add_task_txt, null, false)
        val editTxt = dialogView.findViewById<EditText>(R.id.new_task_txt)

        with(builder) {
            setCancelable(false)
            setFinishOnTouchOutside(false)
            setView(dialogView)
            setPositiveButton(getString(R.string.add_task)) { _, _ ->
                viewModel.insertTask(editTxt.text.trim().toString())
            }

            setNegativeButton(getString(android.R.string.cancel)) { _, _ ->
                create().hide()
            }

        }

        val alertDialog: AlertDialog = builder.create()
        //Display once one instance of the Alert dialog
        if (!alertDialog.isShowing) alertDialog.show()
    }
}