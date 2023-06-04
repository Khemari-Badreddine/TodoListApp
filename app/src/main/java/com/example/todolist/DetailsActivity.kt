package com.example.todolist

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.HelperClasses.detailsAdapter
import com.example.todolist.HelperClasses.detailsHelperClass
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DetailsActivity : AppCompatActivity() {

    var detailsList : MutableList<detailsHelperClass> = mutableListOf()
    lateinit var sharedPreferences: SharedPreferences

    lateinit var editor: SharedPreferences.Editor
    lateinit var detailsRecyclerView: RecyclerView

    var outid : Int = 0
    private var i = -1



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

        detailsList = getListFromSharedPreferences(outid)


        var adapter = detailsAdapter(this, detailsList)


        detailsRecyclerView.adapter = adapter
        detailsRecyclerView.layoutManager = LinearLayoutManager(this)




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
            detailsList.add(detailsHelperClass(generateUniqueId(), text, 0,50,"12 july","3/5",outid))


        }.setNegativeButton("Cancel", null)
        dialog = builder.create()
        dialog.show()


    }




    fun getListFromSharedPreferences(position : Int): MutableList<detailsHelperClass> {
        val json = sharedPreferences.getString("detailsList_${position}", null)

        val gson = Gson()
        val type = object : TypeToken<MutableList<detailsHelperClass>>() {}.type

        return gson.fromJson(json, type) ?: mutableListOf()
    }

    private fun generateUniqueId(): Int {
        i++
        return i
    }
}