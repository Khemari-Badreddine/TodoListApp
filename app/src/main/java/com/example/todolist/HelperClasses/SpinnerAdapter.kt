package com.codinginflow.customspinnerexample

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.todolist.R

class SpinnerAdapter(context: Context, statusList: ArrayList<String?>) :
    ArrayAdapter<String?>(context, 0, statusList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertedView = convertView
        if (convertedView == null) {
            convertedView = LayoutInflater.from(context).inflate(
                android.R.layout.simple_spinner_dropdown_item, parent, false
            )
        }

        val textViewName = convertedView!!.findViewById<TextView>(android.R.id.text1)
        val currentItem: String? = getItem(position)
        if (currentItem != null) {
            textViewName.text = currentItem
        }
        return convertedView
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertedView = convertView
        if (convertedView == null) {
            convertedView = LayoutInflater.from(context).inflate(
                R.layout.spinner_row, parent, false
            )
        }

        val textViewName = convertedView!!.findViewById<TextView>(R.id.name)
        val currentItem: String? = getItem(position)
        if (currentItem != null) {
            textViewName.text = currentItem
            var color: Int = 0

            when (currentItem) {
                "In Progress" -> {
                    color = ContextCompat.getColor(context, R.color.orange)
                    convertedView.setBackgroundResource(R.drawable.progress_bg)

                }

                "Pending" -> {
                    color = ContextCompat.getColor(context, R.color.redish)
                    convertedView.setBackgroundResource(R.drawable.pending_bg)

                }

                "Done" -> {
                    color = ContextCompat.getColor(context, R.color.green)
                    convertedView.setBackgroundResource(R.drawable.done_bg)
                }
            }

            textViewName.setTextColor(color)

        }
        return convertedView
    }
}
