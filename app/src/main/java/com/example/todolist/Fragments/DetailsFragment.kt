package com.example.todolist.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.Database.Details
import com.example.todolist.HelperClasses.detailsAdapter
import com.example.todolist.HelperClasses.todoListAdapter
import com.example.todolist.MainActivity
import com.example.todolist.R
import com.example.todolist.TodoDatabase.todoListApplication
import com.example.todolist.TodoDatabase.todoListViewModel
import com.example.todolist.databinding.FragmentDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking

class detailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null

    lateinit var detailsRecyclerView: RecyclerView
    var todoId: Int? = 0
    var detailsId: Int? = 0
    lateinit var madapter: detailsAdapter


    private lateinit var context: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
    }


    private val mtodoListViewModel: todoListViewModel by viewModels {
        todoListViewModel.todoListViewModelFactory((requireContext().applicationContext as todoListApplication).repository)
    }


    private suspend fun getNextDetailsId(): Int = mtodoListViewModel.getNextDetailsId()

    private suspend fun deletestepswithid(id: Int) {
        mtodoListViewModel.deletestepswithid(id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        todoId = arguments?.getInt("todoId")
        detailsId = runBlocking { getNextDetailsId() }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_details, container, false)

        _binding = FragmentDetailsBinding.bind(view)

        return _binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        if (detailsId == 0) {
            detailsId = sharedPreferences.getInt("detailsId", 1)
        } else {
            editor.putInt("detailsId", detailsId!!)
            editor.apply()
        }


        _binding!!.backiv.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()

        }

        detailsRecyclerView = _binding!!.detailsRv

        madapter = detailsAdapter(context)

        detailsRecyclerView.adapter = madapter
        detailsRecyclerView.layoutManager = LinearLayoutManager(context)


        mtodoListViewModel.alldetails(todoId!!).observe(viewLifecycleOwner) { details ->
            details?.let {
                madapter.setData(it[0].details)
            }
            _binding!!.todotv.text = details[0].todo.title

        }


        madapter.setOnItemClickListener(object : detailsAdapter.onItemClickListener {

            override fun onItemClick(
                position: Int,
                title: String,
                description: String?,
                status: Int,
                date: String,
                stepsdone: String,
                steps: String,
                progress: Int,
                indicator: Int
            ) {

                val details = Details(
                    position,
                    todoId!!,
                    title,
                    description!!,
                    status,
                    date,
                    stepsdone,
                    steps,
                    progress
                )

                if (indicator == 0) {
                    Toast.makeText(
                        requireContext(),
                        "clicked: ${position} with status: ${status} and title: ${title} ",
                        Toast.LENGTH_SHORT
                    ).show()
                    val bundle = Bundle()
                    bundle.putInt("detailsId", position)
                    bundle.putInt("todoId", todoId!!)

                    findNavController().navigate(
                        R.id.stepsFragment,
                        bundle
                    )

                } else if (indicator == 1) {
                    mtodoListViewModel.updatedetails(details)
                } else if (indicator == 2) {
                    mtodoListViewModel.deletedetails(position)
                    runBlocking { deletestepswithid(position) }

                }
            }
        })


        _binding!!.addFAB.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("todoId", todoId!!)
            bundle.putInt("detailsId", detailsId!!)

            findNavController().navigate(
                R.id.action_detailsFragment_to_createTaskFragment,
                bundle
            )
        }

        /* val filterfab = findViewById<FloatingActionButton>(R.id.filterFAB)
         filterfab.setOnClickListener {
             showDialog()
         }*/

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

    }

    private fun searchDatabase(query: String) {
        val searchQuery = "%$query%"

        println("todoId: " + todoId)
        mtodoListViewModel.searchtdetailstable(searchQuery, todoId!!)
            .observe(viewLifecycleOwner) { list ->
                list.let {
                    madapter.setData(list)
                }
            }
    }

}

