package com.example.todolist.HelperClasses;


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.Database.Details
import com.example.todolist.R
import com.example.todolist.Relations.TodoWithDetails
import kotlin.properties.Delegates


class detailsAdapter(private val context: Context) :
    RecyclerView.Adapter<detailsAdapter.MyViewHolder>() {

    private var todoList = emptyList<TodoWithDetails>()


    private lateinit var mlistener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(
            position: Int,
            title: String,
            description: String?,
            status: Int,
            date: String,
            steps: String,
            indicator: Int
        )
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mlistener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.details_card, parent, false),
            this
        )
    }

    override fun getItemCount(): Int {
        return if (todoList.isNotEmpty() && todoList[0].details.isNotEmpty()) {
          //  println("===========NOT EMMPTYYYY=============")
            todoList[0].details.size
        } else {
           // println("===========EMMPTYYYY=============")
            0
        }
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = todoList[0].details

        holder.id = currentItem[position].id
        holder.description = currentItem[position].description
        holder.title.text = currentItem[position].title
        holder.date.text = currentItem[position].date
        holder.steps.text = currentItem[position].steps
        holder.spinner.setSelection(currentItem[position].status)

        holder.bind(currentItem[position])

    }

    class MyViewHolder(itemView: View, private val adapter: detailsAdapter) :
        RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val date: TextView = itemView.findViewById(R.id.datetv)
        val steps: TextView = itemView.findViewById(R.id.stepstv)
        val spinner: Spinner = itemView.findViewById(R.id.status_spinner)
        var description : String? = null
        val delete: ImageView = itemView.findViewById(R.id.delete)
        var id by Delegates.notNull<Int>()

        init {

            itemView.setOnClickListener {

                adapter.mlistener.onItemClick(
                    id,
                    title.text.toString(),
                    description,
                    getSpinnerPosition(spinner.selectedItem.toString()),
                    date.text.toString(),
                    steps.text.toString(),
                    0
                )

            }
        }

        private fun getSpinnerPosition(state: String): Int {
            return when (state) {
                "In Progress" -> 0
                "Pending" -> 1
                "Done" -> 2
                else -> 0
            }
        }

        fun bind(item: Details) {
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


                    adapter.mlistener.onItemClick(
                        item.id,

                        title.text.toString(),
                        description,
                        getSpinnerPosition(spinner.selectedItem.toString()),
                        date.text.toString(),
                        steps.text.toString(),
                        1
                    )


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
            delete.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    adapter.mlistener.onItemClick(
                        item.id,
                        title.text.toString(),
                        description,
                        getSpinnerPosition(spinner.selectedItem.toString()),
                        date.text.toString(),
                        steps.text.toString(),
                        2
                    )

                }

            }


        }


    }


    fun setData(todoList: List<TodoWithDetails>) {
        this.todoList = todoList
        notifyDataSetChanged()
    }
}
