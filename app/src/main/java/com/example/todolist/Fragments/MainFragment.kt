package com.example.todolist.Fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codinginflow.customspinnerexample.SpinnerAdapter
import com.example.todolist.Database.SpinnerItem
import com.example.todolist.Database.Todo
import com.example.todolist.HelperClasses.StepsAdapter
import com.example.todolist.HelperClasses.todoListAdapter
import com.example.todolist.R
import com.example.todolist.TodoDatabase.todoListApplication
import com.example.todolist.TodoDatabase.todoListViewModel
import com.example.todolist.databinding.FragmentMainBinding
import kotlinx.coroutines.runBlocking


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    lateinit var madapter: todoListAdapter
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

        madapter = todoListAdapter(context)


        mainRecyclerView.adapter = madapter
        mainRecyclerView.layoutManager = LinearLayoutManager(context)

        val searchView = _binding!!.search
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnClickListener { searchView?.setIconified(false) }
        searchView.setQueryHint("Search..")
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    searchDatabase(query)
                }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query != null) {
                    searchDatabase(query)
                }
                return true
            }
        })


        mtodoListViewModel.alltodo.observe(viewLifecycleOwner) { todo ->
            todo.let {
                madapter.setData(it)
            }
        }

        _binding!!.floatingActionButton.setOnClickListener {
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

    private fun searchDatabase(query: String) {
        val searchQuery = "%$query%"

        mtodoListViewModel.searchtodoListtable(searchQuery).observe(viewLifecycleOwner) { list ->
            list.let {
                madapter.setData(list)
            }
        }
    }
}