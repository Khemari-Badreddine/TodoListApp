package com.example.todolist

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.HelperClasses.todoListAdapter
import com.example.todolist.TodoDatabase.Todo
import com.example.todolist.TodoDatabase.todoListApplication
import com.example.todolist.TodoDatabase.todoListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var mainRecyclerView: RecyclerView


    private val mtodoListViewModel: todoListViewModel by viewModels {
        todoListViewModel.todoListViewModelFactory((application as todoListApplication).repository)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        editor = sharedPreferences.edit()

        mainRecyclerView = findViewById(R.id.mainRv)

        val madapter = todoListAdapter(this)


        mainRecyclerView.adapter = madapter
        mainRecyclerView.layoutManager = LinearLayoutManager(this)

        mtodoListViewModel.alltodo.observe(this) { todo ->
            todo.let {
                madapter.setData(it)
            }
        }

        madapter.setOnItemClickListener(object : todoListAdapter.onItemClickListener {

            override fun onItemClick(position: Int, title: String, status: Int, indicator: Int) {
                val todo = Todo(position, title, status)


                if (indicator == 0) {
                    Toast.makeText(
                        this@MainActivity,
                        "clicked: ${position} with status: ${status} and title: ${title} ",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@MainActivity, DetailsActivity::class.java)
                    intent.putExtra("heading", title)
                    intent.putExtra("outid", position)
                    startActivity(intent)
                } else if (indicator == 1) {
                    mtodoListViewModel.updatetodo(todo)
                } else if (indicator == 2) {
                    mtodoListViewModel.deletetodo(todo)
                    mtodoListViewModel.deleteTodoWithDetails(position)

                }
            }
        })

        val fab = findViewById<FloatingActionButton>(R.id.floating_action_button)
        fab.setOnClickListener {
            showDialog()
        }

    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(this)
        var dialog = builder.create()


        builder.setView(R.layout.my_dialog_layout).setPositiveButton("OK") { _, _ ->
            val editText = dialog.findViewById<EditText>(R.id.my_edit_text)
            val text = editText?.text.toString()

            val todo = Todo(0, text, 0)

            mtodoListViewModel.addtodo(todo)


        }.setNegativeButton("Cancel", null)
        dialog = builder.create()
        dialog.show()


    }

}

annotation class AndroidEntryPoint
