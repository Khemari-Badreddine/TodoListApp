package com.example.todolist.HelperClasses;


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.codinginflow.customspinnerexample.SpinnerAdapter
import com.example.todolist.Database.Details
import com.example.todolist.R
import kotlin.properties.Delegates

class detailsAdapter(private val context: Context) :
    RecyclerView.Adapter<detailsAdapter.MyViewHolder>() {

    private var todoList = emptyList<Details>()


    private lateinit var mlistener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(
            position: Int,
            title: String,
            description: String?,
            status: Int,
            date: String,
            stepsdone: String,
            steps: String,
            progress: Int,
            indicator: Int
        )
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mlistener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.details_card, parent, false),
            this, context
        )
    }

    override fun getItemCount(): Int {
        return if (todoList.isNotEmpty()) {
            todoList.size
        } else {
            0
        }
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = todoList[position]

        holder.id = currentItem.id
        holder.description = currentItem.description
        holder.title.text = currentItem.title
        holder.date.text = currentItem.date
        holder.steps.text = currentItem.steps
        holder.stepsdone = currentItem.col_stepsdone
        holder.progressBar.progress = currentItem.progress
        holder.spinner.setSelection(currentItem.status)
        holder.spinner.isEnabled = false

        holder.bind(currentItem)

    }

    class MyViewHolder(
        itemView: View,
        private val adapter: detailsAdapter,
        private val context: Context
    ) :
        RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val date: TextView = itemView.findViewById(R.id.datetv)
        val steps: TextView = itemView.findViewById(R.id.stepstv)
        val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
        val spinner: Spinner = itemView.findViewById(R.id.status_spinner)
        var description: String? = null
        val delete: ImageView = itemView.findViewById(R.id.delete)
        var id by Delegates.notNull<Int>()
        var stepsdone by Delegates.notNull<String>()

        private var madapter: SpinnerAdapter
        private var statusList: ArrayList<String?> = arrayListOf("In Progress", "Pending", "Done")

        init {
            madapter = SpinnerAdapter(context, statusList)
            spinner.adapter = madapter

            itemView.setOnClickListener {

                adapter.mlistener.onItemClick(
                    id,
                    title.text.toString(),
                    description,
                    getSpinnerPosition(spinner.selectedItem.toString()),
                    date.text.toString(),
                    stepsdone,
                    steps.text.toString(),
                    progressBar.progress,
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
                        description,
                        getSpinnerPosition(spinner.selectedItem.toString()),
                        date.text.toString(),
                        stepsdone,
                        steps.text.toString(),
                        progressBar.progress,
                        1
                    )

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
                        stepsdone,
                        steps.text.toString(),
                        progressBar.progress,
                        2
                    )

                }

            }


        }

    }


    fun setData(todoList: List<Details>) {
        this.todoList = todoList
        notifyDataSetChanged()
    }
}
