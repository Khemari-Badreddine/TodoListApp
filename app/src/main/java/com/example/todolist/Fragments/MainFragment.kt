package com.example.todolist.Fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
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
import com.example.todolist.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    lateinit private var madapter: todoListAdapter
    private lateinit var context: Context
    lateinit private var mainRecyclerView: RecyclerView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
    }

    private val mtodoListViewModel: todoListViewModel by viewModels {
        // Creating an instance of todoListViewModel using the custom factory
        todoListViewModel.todoListViewModelFactory((requireContext().applicationContext as todoListApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflating the layout for the fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        _binding = FragmentMainBinding.bind(view)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initializing the RecyclerView and its adapter
        mainRecyclerView = _binding!!.mainRv
        madapter = todoListAdapter(context)
        mainRecyclerView.adapter = madapter
        mainRecyclerView.layoutManager = LinearLayoutManager(context)

        // Setting up the search functionality
        val searchView = _binding!!.search
        searchView.isSubmitButtonEnabled = true
        searchView.setOnClickListener { searchView.setIconified(false) }
        searchView.setQueryHint("Search..")
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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

        // Observing the todoList LiveData and updating the adapter when data changes
        mtodoListViewModel.alltodo.observe(viewLifecycleOwner) { todo ->
            todo.let {
                madapter.setData(it)
            }
        }

        // Handling click events on the items in the RecyclerView
        madapter.setOnItemClickListener(object : todoListAdapter.onItemClickListener {
            override fun onItemClick(position: Int, title: String, status: Int, indicator: Int) {
                val todo = Todo(position, title, status)

                if (indicator == 0) {
                    // Navigating to the details fragment when an item is clicked
                    val bundle = Bundle()
                    bundle.putInt("todoId", position)
                    findNavController().navigate(R.id.detailsFragment, bundle)
                } else if (indicator == 1) {
                    // Updating the todo item when an item is clicked
                    mtodoListViewModel.updatetodo(todo)
                } else if (indicator == 2) {
                    // Deleting the todo item and associated details and steps when an item is clicked
                    mtodoListViewModel.deletetodo(todo)
                    mtodoListViewModel.deleteTodoWithDetails(position)
                    mtodoListViewModel.deleteTodoWithSteps(position)
                }
            }
        })

        // Showing a dialog to add a new todo item
        _binding!!.floatingActionButton.setOnClickListener {
            showDialog()
        }
    }

    // Function to show the dialog for adding a new todo item
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

    // Function to search the database for matching todo items
    private fun searchDatabase(query: String) {
        val searchQuery = "%$query%"
        mtodoListViewModel.searchtodoListtable(searchQuery).observe(viewLifecycleOwner) { list ->
            list.let {
                madapter.setData(list)
            }
        }
    }
}
