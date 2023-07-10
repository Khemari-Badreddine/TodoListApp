package com.example.todolist.HelperClasses

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.Database.Details
import com.example.todolist.Database.Steps
import com.example.todolist.Database.Todo
import com.example.todolist.R
import com.example.todolist.Relations.DetailsWithSteps
import kotlin.properties.Delegates


class CreateTaskStepsAdapter(private val context: Context) :
    RecyclerView.Adapter<CreateTaskStepsAdapter.MyViewHolder>() {

    private var todoList = mutableListOf<Steps>()

    private lateinit var mlistener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(step: Steps, steps: MutableList<Steps>, changedstring : String,adapterposition: Int, indicator: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mlistener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.steps_card, parent, false),
            this
        )
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = todoList[position]

        holder.stepsnum.text = currentItem.stepNum.toString()
        holder.title.setText(currentItem.title)
        holder.description.setText(currentItem.description)

        holder.bind(currentItem,todoList)


    }

    class MyViewHolder(itemView: View, private val adapter: CreateTaskStepsAdapter) :
        RecyclerView.ViewHolder(itemView) {
        val title: EditText = itemView.findViewById(R.id.title_et)
        val description: EditText = itemView.findViewById(R.id.description_et)
        val stepsnum: TextView = itemView.findViewById(R.id.steps_numbertv)
        val delete: View = itemView.findViewById(R.id.deletev)
        var id by Delegates.notNull<Int>()

        fun bind(item: Steps,steps: MutableList<Steps>) {

            delete.setOnClickListener {

               var Ids= mutableListOf<Int>()

                for (step in steps) {
                }

                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    adapter.mlistener.onItemClick(
                        item,
                        steps,
                        title.toString(),
                        adapterPosition,
                        1
                    )

                }

            }

            title.addTextChangedListener(object : TextWatcher {

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // No need to implement
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    adapter.mlistener.onItemClick(
                        item,
                        steps,
                        s?.toString() ?: "",
                        adapterPosition,
                        2
                    )

                }
                override fun afterTextChanged(s: Editable?) {

                }
            })

            description.addTextChangedListener(object : TextWatcher {

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // No need to implement
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    adapter.mlistener.onItemClick(
                        item,
                        steps,
                        s?.toString() ?: "",
                        adapterPosition,
                        3
                    )

                }

                override fun afterTextChanged(s: Editable?) {
                    // No need to implement
                }
            })


        }
    }


    fun setData(todoList: MutableList<Steps>) {
        this.todoList = todoList
        notifyDataSetChanged()
    }
}
