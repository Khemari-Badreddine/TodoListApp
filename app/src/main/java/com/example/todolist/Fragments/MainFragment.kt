package com.example.todolist.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.Database.Todo
import com.example.todolist.HelperClasses.todoListAdapter

import com.example.todolist.R
import com.example.todolist.TodoDatabase.todoListApplication
import com.example.todolist.TodoDatabase.todoListViewModel
import com.example.todolist.databinding.FragmentDetailsBinding
import com.example.todolist.databinding.FragmentMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null

    private lateinit var context: Context

    lateinit var mainRecyclerView: RecyclerView


    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
    }

    private val mtodoListViewModel: todoListViewModel by viewModels {
        todoListViewModel.todoListViewModelFactory((requireContext().applicationContext as todoListApplication).repository)
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        _binding = FragmentMainBinding.bind(view)


        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainRecyclerView = _binding!!.mainRv

        val madapter = todoListAdapter(context)


        mainRecyclerView.adapter = madapter
        mainRecyclerView.layoutManager = LinearLayoutManager(context)

        mtodoListViewModel.alltodo.observe(viewLifecycleOwner) { todo ->
            todo.let {
                madapter.setData(it)
            }
        }

        _binding!!.floatingActionButton.setOnClickListener{
            showDialog()
        }

        madapter.setOnItemClickListener(object : todoListAdapter.onItemClickListener {

            override fun onItemClick(position: Int, title: String, status: Int, indicator: Int) {
                val todo = Todo(position, title, status)

                if (indicator == 0) {
                    Toast.makeText(
                        requireContext(),
                        "clicked: ${position} with status: ${status} and title: ${title} ",
                        Toast.LENGTH_SHORT
                    ).show()

                    val bundle = Bundle()
                    bundle.putString("heading", title)
                    println("============"+position+"==============")
                    bundle.putInt("todoId", position!!)

                    findNavController().navigate(
                        R.id.detailsFragment,
                        bundle
                    )

                } else if (indicator == 1) {
                    mtodoListViewModel.updatetodo(todo)
                } else if (indicator == 2) {
                    mtodoListViewModel.deletetodo(todo)
                    mtodoListViewModel.deleteTodoWithDetails(position)
                    mtodoListViewModel.deleteTodoWithSteps(position)

                }
            }

        })
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(context)
        var dialog = builder.create()


        builder.setView(R.layout.my_dialog_layout).setPositiveButton("OK") { _, _ ->
            val editText = dialog.findViewById<EditText>(R.id.my_edit_text)
            val text = editText?.text.toString()

            val todo = Todo(0, text, 0)

            mtodoListViewModel.addtodo(todo)


        }.setNegativeButton("Cancel", null)
        dialog = builder.create()
        dialog.show()


    }


}