package com.example.todolist.Fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.HelperClasses.CreateTaskStepsAdapter
import com.example.todolist.HelperClasses.StepsAdapter
import com.example.todolist.R
import com.example.todolist.TodoDatabase.todoListApplication
import com.example.todolist.TodoDatabase.todoListViewModel
import com.example.todolist.databinding.FragmentCreateTaskBinding
import com.example.todolist.databinding.FragmentDetailsBinding
import com.example.todolist.databinding.FragmentStepsBinding
import kotlinx.coroutines.runBlocking

class StepsFragment : Fragment() {

    private var _binding: FragmentStepsBinding? = null


    var heading: String? = null
    var detailsId: Int? = 0
    var todoId: Int? = 0


    private lateinit var context: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
    }

    private val mtodoListViewModel: todoListViewModel by viewModels {
        todoListViewModel.todoListViewModelFactory((requireContext().applicationContext as todoListApplication).repository)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        heading = arguments?.getString("heading")
        detailsId = arguments?.getInt("detailsId")
        todoId = arguments?.getInt("todoId")

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_steps, container, false)
        _binding = FragmentStepsBinding.bind(view)

        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val stepsAdapter = StepsAdapter(requireContext())
        _binding!!.stepsRv.adapter = stepsAdapter
        _binding!!.stepsRv.layoutManager = LinearLayoutManager(requireContext())

        mtodoListViewModel.DetailsWithSteps(detailsId!!).observe(viewLifecycleOwner) { steps ->
            steps?.let {
                if (steps[0].steps.isNotEmpty()) {
                    stepsAdapter.setData(steps[0].steps.toMutableList())
                }

                _binding!!.titletv.text = steps[0].details.title
                _binding!!.descriptiontv.text = steps[0].details.description
                _binding!!.datetv.text = steps[0].details.date
                _binding!!.stepstv.text = steps[0].details.steps
                _binding!!.statusSpinner.setSelection(steps[0].details.status)

            }
        }

        _binding!!.backiv.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("heading", heading)
            bundle.putInt("todoId", todoId!!)
            findNavController().navigate(
                R.id.detailsFragment,
                bundle
            )
        }

        _binding!!.editFAB.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("detailsId", detailsId!!)
            bundle.putString("heading", heading)
            bundle.putInt("todoId", todoId!!)

            findNavController().navigate(
                R.id.editStepsFragment,
                bundle
            )
        }


    }

}