package com.example.todolist

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.HelperClasses.mainAdapter
import com.example.todolist.HelperClasses.mainHelperClass
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken


class MainActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    private var i = -1

    lateinit var editor: SharedPreferences.Editor
    lateinit var mainRecyclerView: RecyclerView
    var todoList: MutableList<mainHelperClass> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        editor = sharedPreferences.edit()

        mainRecyclerView = findViewById(R.id.mainRv)

        todoList = getListFromSharedPreferences()


        var adapter = mainAdapter(this, todoList)


        mainRecyclerView.adapter = adapter
        mainRecyclerView.layoutManager = LinearLayoutManager(this)


        adapter.setOnItemClickListener(object : mainAdapter.onItemClickListener {

            override fun onItemClick(position: Int) {

               // Toast.makeText(this@MainActivity, "clicked: ${position}", Toast.LENGTH_SHORT).show()
                val intent = Intent( this@MainActivity ,DetailsActivity::class. java)
                intent.putExtra("heading",todoList[position].title)
                intent.putExtra("outid",position)
                startActivity(intent)

            }


        }


        )


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
            var text = editText?.text.toString()
            todoList.add(mainHelperClass(generateUniqueId(), text, 0))


        }.setNegativeButton("Cancel", null)
        dialog = builder.create()
        dialog.show()


    }


    fun saveStringListToSharedPreferences(stringList: MutableList<String>) {
        val gson = Gson()

        val jsonArray1 = gson.toJsonTree(stringList).asJsonArray
        val jsonString2 = sharedPreferences.getString("List", null)

        var jsonArray2 = if (!jsonString2.isNullOrEmpty()) {
            gson.fromJson(jsonString2, JsonArray::class.java)

        } else {
            JsonArray()
        }

        println("=====INSIDE 1====" + jsonArray1 + "=========")

        println("=====INSIDE 2====" + jsonArray2 + "=========")


        var mergedJsonArray = JsonArray()
        if (!jsonArray2.isJsonNull) {
            mergedJsonArray.addAll(jsonArray2)
        }
        if (!jsonArray1.isJsonNull) {
            mergedJsonArray.addAll(jsonArray1)
        }

        println("=====INSIDE MERG====" + mergedJsonArray + "=========")


        val mergedJsonString = mergedJsonArray.toString()

        editor.putString("List", mergedJsonString)
        editor.apply()
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
