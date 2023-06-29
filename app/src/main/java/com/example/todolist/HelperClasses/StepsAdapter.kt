package com.example.todolist.HelperClasses

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.Database.Steps
import com.example.todolist.Database.Todo
import com.example.todolist.R
import com.example.todolist.Relations.DetailsWithSteps
import kotlin.properties.Delegates


class StepsAdapter(private val context: Context) :
    RecyclerView.Adapter<StepsAdapter.MyViewHolder>() {

    private var todoList = emptyList<Steps>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.step_card, parent, false),
            this
        )
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = todoList[position]

        holder.id = currentItem.id
        holder.stepsnum.text = currentItem.stepNum.toString()
        holder.spinner.setSelection(currentItem.status)
        holder.title.setText(currentItem.title)
        holder.description.setText(currentItem.description)

        holder.bind(currentItem)


    }

    class MyViewHolder(itemView: View, private val adapter: StepsAdapter) :
        RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val description: TextView = itemView.findViewById(R.id.descriptiontv)
        val spinner: Spinner = itemView.findViewById(R.id.status_spinner)
        val stepsnum: TextView = itemView.findViewById(R.id.steps_numbertv)
        var id by Delegates.notNull<Int>()

        fun bind(item: Steps) {
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

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do nothing
                }


                private fun getSpinnerPosition(state: String): Int {
                    return when (state) {
                        "In Progress" -> 0
                        "Pending" -> 1
                        "Done" -> 2
                        else -> 0
                    }
                }
            }

        }


    }


    fun setData(todoList: List<Steps>) {
        this.todoList = todoList
        notifyDataSetChanged()
    }
}
