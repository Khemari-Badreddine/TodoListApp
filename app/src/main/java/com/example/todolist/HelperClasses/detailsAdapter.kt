package com.example.todolist.HelperClasses


import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken


class detailsAdapter(
    private val context: Context,
    private var mainList: MutableList<detailsHelperClass>) : RecyclerView.Adapter<detailsAdapter.mainViewHolder>() {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    var stringList: MutableList<detailsHelperClass> = mutableListOf()


    private lateinit var mlistener : onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mlistener = listener
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): detailsAdapter.mainViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.details_card, parent, false)

        return mainViewHolder(itemView, this)

    }

    override fun onBindViewHolder(holder: detailsAdapter.mainViewHolder, position: Int) {


        val currentItem = mainList[position]
        holder.title.text = currentItem.title
        holder.spinner.setSelection(currentItem.state)
        holder.date.text = currentItem.datetv
        holder.done.text = currentItem.donetv
        holder.progressbar.progress = currentItem.progress


        refreshItemIds()
        println("=========" + mainList + "=========")

        saveItemToSharedPreferences(mainList,currentItem.outid)

        holder.bind(currentItem)

    }

    override fun getItemCount(): Int {
        return mainList.size
    }


    class mainViewHolder(itemView: View, private val adapter: detailsAdapter) : RecyclerView.ViewHolder(itemView) {

        val title: TextView = itemView.findViewById(R.id.title)
        val spinner: Spinner = itemView.findViewById(R.id.status_spinner)
        val date: TextView = itemView.findViewById(R.id.datetv)
        val done: TextView = itemView.findViewById(R.id.donetv)
        val delete: ImageView = itemView.findViewById(R.id.delete)
        var progressbar: ProgressBar = itemView.findViewById(R.id.progressBar)

        init{

            itemView.setOnClickListener{

                adapter.mlistener.onItemClick(adapterPosition)
                refreshItemIds()


            }


        }

        fun bind(item: detailsHelperClass) {
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
                    saveItemToSharedPreferences(adapter.mainList,item.outid)

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
                    saveItemToSharedPreferences(adapter.mainList,item.outid)
                    // Notify the adapter that an item has been removed
                    adapter.notifyItemRemoved(position)
                    // Show a toast or perform any other action with the deletedItem if needed
                    Toast.makeText(coontext, "Deleted: ${deletedItem.title}", Toast.LENGTH_SHORT)
                        .show()
                }

                refreshItemIds()

                println(item.id)
                println("===AFTER DELETE======" + adapter.mainList + "=========")

                saveItemToSharedPreferences(adapter.mainList,item.outid)

            }


        }


        fun refreshItemIds() {
            for (index in adapter.mainList.indices) {
                adapter.mainList[index].id = index
            }
        }

        fun saveItemToSharedPreferences(List: MutableList<detailsHelperClass>,outid : Int) {
            val sharedPreferences =
                adapter.context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            val gson = Gson()
            val json = gson.toJson(List)

            editor.putString("detailsList_${outid}", json)
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

        fun getListFromSharedPreferences(): MutableList<detailsHelperClass> {

            val sharedPreferences =
                adapter.context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

            val json = sharedPreferences.getString("List", null)

            val gson = Gson()
            val type = object : TypeToken<MutableList<detailsHelperClass>>() {}.type

            return gson.fromJson(json, type) ?: mutableListOf()
        }

    }


    fun setListFromSharedPreferences() {
        val json = sharedPreferences.getString("MyPrefs", null)
        val gson = Gson()
        val type = object : TypeToken<List<detailsHelperClass>>() {}.type
        val savedList: List<detailsHelperClass> = gson.fromJson(json, type) ?: emptyList()

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

    fun saveItemToSharedPreferences(List: MutableList<detailsHelperClass>,outid : Int) {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val gson = Gson()
        val json = gson.toJson(List)

        editor.putString("detailsList_${outid}", json)
        editor.apply()
    }

    fun getListFromSharedPreferences(): MutableList<detailsHelperClass> {

        val json = sharedPreferences.getString("List", null)

        val gson = Gson()
        val type = object : TypeToken<MutableList<detailsHelperClass>>() {}.type

        return gson.fromJson(json, type) ?: mutableListOf()
    }

    fun refreshItemIds() {
        for (index in mainList.indices) {
            mainList[index].id = index
        }
    }


}
