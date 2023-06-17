package com.example.todolist

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.DetailsDatabase.Details
import com.example.todolist.HelperClasses.detailsAdapter
import com.example.todolist.HelperClasses.todoListAdapter
import com.example.todolist.TodoDatabase.Todo
import com.example.todolist.TodoDatabase.todoListApplication
import com.example.todolist.TodoDatabase.todoListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DetailsActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences

    lateinit var editor: SharedPreferences.Editor
    lateinit var detailsRecyclerView: RecyclerView

    var outid : Int = 0

    private val mtodoListViewModel: todoListViewModel by viewModels {
        todoListViewModel.todoListViewModelFactory((application as todoListApplication).repository)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val todotv:TextView = findViewById(R.id.todotv)
        val backiv: ImageView = findViewById(R.id.backarrow)

        val bundle : Bundle?= intent.extras
        val heading = bundle!!.getString("heading")

        todotv.setText(heading)
        outid = bundle!!.getInt("outid")

        backiv.setOnClickListener{

            onBackPressed()

        }

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        editor = sharedPreferences.edit()

        detailsRecyclerView = findViewById(R.id.detailsRv)

        val madapter = detailsAdapter(this)


        detailsRecyclerView.adapter = madapter
        detailsRecyclerView.layoutManager = LinearLayoutManager(this)

        mtodoListViewModel.alldetails(outid).observe(this) { details ->
            details.let {
                println("==============="+outid+"===============")
                println("==============="+details+"===============")


                madapter.setData(it)

            }
        }

        madapter.setOnItemClickListener(object : detailsAdapter.onItemClickListener {

            override fun onItemClick(position: Int,title: String, status: Int,date: String,steps: String, indicator: Int) {
                val details = Details(position,outid, title, status,date,steps)


                if (indicator == 0) {
                    Toast.makeText(
                        this@DetailsActivity,
                        "clicked: ${position} with status: ${status} and title: ${title} ",
                        Toast.LENGTH_SHORT
                    ).show()

                } else if (indicator == 1) {
                    mtodoListViewModel.updatedetails(details)
                } else if (indicator == 2) {
                    mtodoListViewModel.deletedetails(position)
                }
            }
        })



        val addfab = findViewById<FloatingActionButton>(R.id.addFAB)
        addfab.setOnClickListener {
            showDialog()
        }

        val filterfab = findViewById<FloatingActionButton>(R.id.filterFAB)
        filterfab.setOnClickListener {
            showDialog()
        }



    }


    private fun showDialog() {
        val builder = AlertDialog.Builder(this)
        var dialog = builder.create()


        builder.setView(R.layout.my_dialog_layout).setPositiveButton("OK") { _, _ ->
            val editText = dialog.findViewById<EditText>(R.id.my_edit_text)
            var text = editText?.text.toString()
            val detail = Details(0,outid, text, 0,"12 july","3/5")
            mtodoListViewModel.adddetails(detail)

        }.setNegativeButton("Cancel", null)
        dialog = builder.create()
        dialog.show()


    }




}