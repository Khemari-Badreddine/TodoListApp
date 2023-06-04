package com.example.todolist.HelperClasses


import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken


class mainAdapter(
    private val context: Context,
    private var mainList: MutableList<mainHelperClass>) : RecyclerView.Adapter<mainAdapter.mainViewHolder>() {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    var stringList: MutableList<mainHelperClass> = mutableListOf()


    private lateinit var mlistener : onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mlistener = listener
    }




        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mainAdapter.mainViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.main_card, parent, false)

        return mainViewHolder(itemView, this)

    }

    override fun onBindViewHolder(holder: mainAdapter.mainViewHolder, position: Int) {


        val currentItem = mainList[position]
        holder.title.text = currentItem.title
        holder.spinner.setSelection(currentItem.state)

        refreshItemIds()
        println("=========" + mainList + "=========")

        saveItemToSharedPreferences(mainList)

        holder.bind(currentItem)


    }

    override fun getItemCount(): Int {
        return mainList.size
    }


    class mainViewHolder(itemView: View, private val adapter: mainAdapter) : RecyclerView.ViewHolder(itemView) {

        val title: TextView = itemView.findViewById(R.id.title)
        val spinner: Spinner = itemView.findViewById(R.id.status_spinner)
        val delete: ImageView = itemView.findViewById(R.id.delete)

        init{

            itemView.setOnClickListener{

                adapter.mlistener.onItemClick(adapterPosition)
                refreshItemIds()


            }


        }

        fun bind(item: mainHelperClass) {
            // Get the context from the itemView
            val coontext = itemView.context

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {


                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // Get the selected item
                    val selectedItem = spinner.selectedItem.toString()
                    val selectedItemView = view as TextView


                    when (selectedItem) {
                        "In Progress" -> {
                            spinner.setBackgroundResource(R.drawable.progress_bg)
                            val textcolor = ContextCompat.getColor(coontext, R.color.orange)
                            selectedItemView.setTextColor(textcolor)

                        }

                        "Pending" -> {
                            spinner.setBackgroundResource(R.drawable.pending_bg)
                            val textcolor = ContextCompat.getColor(coontext, R.color.redish)
                            selectedItemView.setTextColor(textcolor)

                        }

                        "Done" -> {
                            spinner.setBackgroundResource(R.drawable.done_bg)
                            val textcolor = ContextCompat.getColor(coontext, R.color.green)
                            selectedItemView.setTextColor(textcolor)

                        }
                    }

                    val existingItem = adapter.mainList.find { it.id == item.id }

                    print(item.id)

                    if (existingItem != null) {
                        // Update the existing item in the list
                        existingItem.title = title.text.toString()
                        existingItem.state = getSpinnerPosition(spinner.selectedItem.toString())
                    }

                    refreshItemIds()



                    println("====SPINNER=====" + adapter.mainList + "=========")
                    // Save the updated list to SharedPreferences
                    saveItemToSharedPreferences(adapter.mainList)

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do nothing
                }
            }


            delete.setOnClickListener {
                val position = item.id
                if (position != RecyclerView.NO_POSITION) {
                    // Remove the item from the stringList
                    val deletedItem = adapter.mainList.removeAt(position)
                    // Save the updated list to SharedPreferences
                    saveItemToSharedPreferences(adapter.mainList)
                    // Notify the adapter that an item has been removed
                    adapter.notifyItemRemoved(position)
                    // Show a toast or perform any other action with the deletedItem if needed
                    Toast.makeText(coontext, "Deleted: ${deletedItem.title}", Toast.LENGTH_SHORT)
                        .show()
                }

                refreshItemIds()

                println(item.id)
                println("===AFTER DELETE======" + adapter.mainList + "=========")

                saveItemToSharedPreferences(adapter.mainList)

            }


        }


        fun refreshItemIds() {
            for (index in adapter.mainList.indices) {
                adapter.mainList[index].id = index
            }
        }

        fun saveItemToSharedPreferences(List: MutableList<mainHelperClass>) {
            val sharedPreferences =
                adapter.context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            val gson = Gson()
            val json = gson.toJson(List)

            editor.putString("List", json)
            editor.apply()
        }

        private fun getSpinnerPosition(state: String): Int {
            return when (state) {
                "In Progress" -> 0
                "Pending" -> 1
                "Done" -> 2
                else -> 0 // Default position if state is not recognized
            }
        }

        fun getListFromSharedPreferences(): MutableList<mainHelperClass> {

            val sharedPreferences =
                adapter.context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

            val json = sharedPreferences.getString("List", null)

            val gson = Gson()
            val type = object : TypeToken<MutableList<mainHelperClass>>() {}.type

            return gson.fromJson(json, type) ?: mutableListOf()
        }

    }


    fun setListFromSharedPreferences() {
        val json = sharedPreferences.getString("MyPrefs", null)
        val gson = Gson()
        val type = object : TypeToken<List<mainHelperClass>>() {}.type
        val savedList: List<mainHelperClass> = gson.fromJson(json, type) ?: emptyList()

        mainList.clear()
        mainList.addAll(savedList)
        notifyDataSetChanged()
    }


    private fun getSpinnerPosition(state: String): Int {
        return when (state) {
            "In Progress" -> 0
            "Pending" -> 1
            "Done" -> 2
            else -> 0 // Default position if state is not recognized
        }
    }

    fun saveItemToSharedPreferences(List: MutableList<mainHelperClass>) {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val gson = Gson()
        val json = gson.toJson(List)

        editor.putString("List", json)
        editor.apply()
    }

    fun getListFromSharedPreferences(): MutableList<mainHelperClass> {

        val json = sharedPreferences.getString("List", null)

        val gson = Gson()
        val type = object : TypeToken<MutableList<mainHelperClass>>() {}.type

        return gson.fromJson(json, type) ?: mutableListOf()
    }

    fun refreshItemIds() {
        for (index in mainList.indices) {
            mainList[index].id = index
        }
    }


}
