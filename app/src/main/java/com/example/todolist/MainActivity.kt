package com.example.todolist

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.HelperClasses.mainAdapter
import com.example.todolist.HelperClasses.mainHelperClass
import com.example.todolist.HelperClasses.todoListAdapter
import com.example.todolist.database.Todo
import com.example.todolist.database.todoListApplication
import com.example.todolist.database.todoListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    private var i = -1

    lateinit var editor: SharedPreferences.Editor
    lateinit var mainRecyclerView: RecyclerView
    var todoList: MutableList<mainHelperClass> = mutableListOf()


    private val mtodoListViewModel: todoListViewModel by viewModels {
        todoListViewModel.todoListViewModelFactory((application as todoListApplication).repository)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        editor = sharedPreferences.edit()

        mainRecyclerView = findViewById(R.id.mainRv)

        //   todoList = getListFromSharedPreferences()


        //val adapter = mainAdapter(this, todoList)
        val madapter = todoListAdapter(this)


        mainRecyclerView.adapter = madapter
        mainRecyclerView.layoutManager = LinearLayoutManager(this)

        mtodoListViewModel.alltodo.observe(this) { todo ->
            todo.let {
                madapter.setData(it)

            }
        }

        //  mtodoListViewModel = ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(todoListViewModel::class.java)


        /*  adapter.setOnItemClickListener(object : mainAdapter.onItemClickListener {

              override fun onItemClick(position: Int) {

                  // Toast.makeText(this@MainActivity, "clicked: ${position}", Toast.LENGTH_SHORT).show()
                  val intent = Intent(this@MainActivity, DetailsActivity::class.java)
                  intent.putExtra("heading", todoList[position].title)
                  intent.putExtra("outid", position)
                  startActivity(intent)

              }
          })*/


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
            todoList.add(mainHelperClass(generateUniqueId(), text, 0))

            val todo = Todo(0,text,1)

            mtodoListViewModel.addtodo(todo)


        }.setNegativeButton("Cancel", null)
        dialog = builder.create()
        dialog.show()


    }


    fun getListFromSharedPreferences(): MutableList<mainHelperClass> {
        val json = sharedPreferences.getString("List", null)

        val gson = Gson()
        val type = object : TypeToken<MutableList<mainHelperClass>>() {}.type

        return gson.fromJson(json, type) ?: mutableListOf()
    }

    private fun generateUniqueId(): Int {
        i++
        return i
    }


}

annotation class AndroidEntryPoint
