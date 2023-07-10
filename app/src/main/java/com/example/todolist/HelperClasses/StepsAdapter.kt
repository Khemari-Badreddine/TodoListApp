package com.example.todolist.HelperClasses

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.codinginflow.customspinnerexample.SpinnerAdapter
import com.example.todolist.Database.Steps
import com.example.todolist.R
import kotlin.properties.Delegates

class StepsAdapter(private val context: Context) :
    RecyclerView.Adapter<StepsAdapter.MyViewHolder>() {

    private var todoList = emptyList<Steps>()


    private lateinit var mlistener: onItemClickListener
    interface onItemClickListener {
        fun onItemClick(
            id: Int,
            detailsId: Int,
            todoId: Int,
            stepNum: Int,
            title: String?,
            description: String?,
            status: Int,
        )
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mlistener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.step_card, parent, false),
            this,context
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

    class MyViewHolder(itemView: View, private val adapter: StepsAdapter,private val context: Context) :
        RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val description: TextView = itemView.findViewById(R.id.descriptiontv)
        val spinner: Spinner = itemView.findViewById(R.id.status_spinner)
        val stepsnum: TextView = itemView.findViewById(R.id.steps_numbertv)
        var id by Delegates.notNull<Int>()

        private var madapter: SpinnerAdapter
        private var statusList: ArrayList<String?> = arrayListOf("In Progress", "Pending", "Done")
        init {
            madapter = SpinnerAdapter(context, statusList)
            spinner.adapter = madapter

        }

        fun bind(item: Steps) {

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    adapter.mlistener.onItemClick(
                        item.id,
                        item.detailsId,
                        item.todoId,
                        item.stepNum,
                        title.text.toString(),
                        description.text.toString(),
                        getSpinnerPosition(spinner.selectedItem.toString()),
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
        }

    }

    fun setData(todoList: List<Steps>) {
        this.todoList = todoList
        notifyDataSetChanged()
    }

}

