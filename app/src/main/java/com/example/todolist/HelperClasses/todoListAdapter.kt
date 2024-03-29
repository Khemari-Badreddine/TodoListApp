package com.example.todolist.HelperClasses;


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.codinginflow.customspinnerexample.SpinnerAdapter
import com.example.todolist.R
import com.example.todolist.Database.Todo
import kotlin.properties.Delegates


class todoListAdapter(private val context: Context) :
    RecyclerView.Adapter<todoListAdapter.MyViewHolder>() {

    private var todoList = emptyList<Todo>()

    private lateinit var mlistener: onItemClickListener


    interface onItemClickListener {
        fun onItemClick(position: Int, title: String, status: Int, indicator: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mlistener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.main_card, parent, false),
            this, context
        )
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = todoList[position]

        holder.id = currentItem.id
        holder.title.text = currentItem.title
        holder.spinner.setSelection(currentItem.status)

        holder.bind(currentItem)

    }

    class MyViewHolder(
        itemView: View,
        private val adapter: todoListAdapter,
        private val context: Context
    ) : RecyclerView.ViewHolder(itemView) {

        val title: TextView = itemView.findViewById(R.id.title)
        val spinner: Spinner = itemView.findViewById(R.id.status_spinner)
        val delete: ImageView = itemView.findViewById(R.id.delete)
        var id by Delegates.notNull<Int>()

        var madapter: SpinnerAdapter
        private var statusList: ArrayList<String?> = arrayListOf("In Progress", "Pending", "Done")


        init {
            madapter = SpinnerAdapter(context, statusList)
            spinner.adapter = madapter

            itemView.setOnClickListener {
                adapter.mlistener.onItemClick(
                    id,
                    title.text.toString(),
                    getSpinnerPosition(spinner.selectedItem.toString()),
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

        fun bind(item: Todo) {

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    adapter.mlistener.onItemClick(
                        item.id,
                        title.text.toString(),
                        getSpinnerPosition(spinner.selectedItem.toString()),
                        1
                    )

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do nothing
                }
            }

            delete.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    adapter.mlistener.onItemClick(
                        item.id,
                        title.text.toString(),
                        getSpinnerPosition(spinner.selectedItem.toString()),
                        2
                    )
                }
            }
        }


    }

    fun setData(todoList: List<Todo>) {
        this.todoList = todoList
        notifyDataSetChanged()
    }
}
