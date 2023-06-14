package com.example.todolist.HelperClasses;


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.database.Todo


class todoListAdapter(private val context: Context) :
    RecyclerView.Adapter<todoListAdapter.MyViewHolder>() {

    private var todoList = emptyList<Todo>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.main_card, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = todoList[position]

        holder.itemView.id = currentItem.id
        holder.title.text = currentItem.title
        holder.spinner.setSelection(currentItem.status)

      //  val todo = todo(currentItem.id,currentItem.title,currentItem.status)
      //  mtodoListViewModel.addtodo(todo)



        holder.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {


            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Get the selected item
                val selectedItem = holder.spinner.selectedItem.toString()
                val selectedItemView = view as TextView


                when (selectedItem) {
                    "In Progress" -> {
                        holder.spinner.setBackgroundResource(R.drawable.progress_bg)
                        val textcolor = ContextCompat.getColor(context, R.color.orange)
                        selectedItemView.setTextColor(textcolor)

                    }

                    "Pending" -> {
                        holder.spinner.setBackgroundResource(R.drawable.pending_bg)
                        val textcolor = ContextCompat.getColor(context, R.color.redish)
                        selectedItemView.setTextColor(textcolor)

                    }

                    "Done" -> {
                        holder.spinner.setBackgroundResource(R.drawable.done_bg)
                        val textcolor = ContextCompat.getColor(context, R.color.green)
                        selectedItemView.setTextColor(textcolor)

                    }
                }


            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val spinner: Spinner = itemView.findViewById(R.id.status_spinner)

    }

    fun setData(todoList: List<Todo>) {
        this.todoList = todoList
        notifyDataSetChanged()
    }
}
