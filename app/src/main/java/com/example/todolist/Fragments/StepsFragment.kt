package com.example.todolist.Fragments

import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.codinginflow.customspinnerexample.SpinnerAdapter
import com.example.todolist.Database.Details
import com.example.todolist.Database.Steps
import com.example.todolist.HelperClasses.StepsAdapter
import com.example.todolist.R
import com.example.todolist.TodoDatabase.todoListApplication
import com.example.todolist.TodoDatabase.todoListViewModel
import com.example.todolist.databinding.FragmentStepsBinding
import kotlinx.coroutines.runBlocking

class StepsFragment : Fragment() {

    private var _binding: FragmentStepsBinding? = null
    lateinit var stepsAdapter: StepsAdapter
    private var statusList: ArrayList<String?> = arrayListOf("In Progress", "Pending", "Done")
    lateinit var adapter: SpinnerAdapter
    var heading: String? = null
    var detailsId: Int? = 0
    var todoId: Int? = 0
    var detailsdescription: String = "null"
    var detailsdate: String = "null"
    var donesteps: String = "null"
    var status: Int = 0
    private var isUserUpdate = false


    private lateinit var context: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
    }

    private val mtodoListViewModel: todoListViewModel by viewModels {
        todoListViewModel.todoListViewModelFactory((requireContext().applicationContext as todoListApplication).repository)
    }

    private suspend fun getNumberOfStepsWithStatus2(): Int =
        mtodoListViewModel.getNumberOfStepsWithStatus2(detailsId!!)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        heading = arguments?.getString("heading")
        detailsId = arguments?.getInt("detailsId")
        todoId = arguments?.getInt("todoId")
        stepsAdapter = StepsAdapter(requireContext())


    }

    private suspend fun getNumberOfStepsInDetail(): Int {
        return mtodoListViewModel.getNumberOfStepsInDetail(detailsId!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_steps, container, false)
        _binding = FragmentStepsBinding.bind(view)

        mtodoListViewModel.DetailsWithSteps(detailsId!!).observe(viewLifecycleOwner) { steps ->
            steps?.let {
                if (steps[0].steps.isNotEmpty()) {
                    stepsAdapter.setData(steps[0].steps.toMutableList())
                }

                detailsdescription = steps[0].details.description
                detailsdate = steps[0].details.date
                donesteps = runBlocking { getNumberOfStepsWithStatus2() }.toString()
                var progress: Int = 0
                if (runBlocking { getNumberOfStepsInDetail() } != 0) {
                    progress =
                        (runBlocking { getNumberOfStepsWithStatus2() } * 100) / runBlocking { getNumberOfStepsInDetail() }
                }

                if (!isUserUpdate) {

                    if (runBlocking { getNumberOfStepsWithStatus2() } == runBlocking { getNumberOfStepsInDetail() }) {

                        mtodoListViewModel.updatedetails(
                            Details(
                                detailsId!!,
                                todoId!!,
                                steps[0].details.title!!,
                                detailsdescription,
                                2,
                                detailsdate,
                                donesteps,
                                "$donesteps/${runBlocking { getNumberOfStepsInDetail() }}",
                                100
                            )
                        )

                        val color = ContextCompat.getColor(context, R.color.green)
                        _binding!!.stepsiv.setColorFilter(color, PorterDuff.Mode.SRC_IN)
                        isUserUpdate = true


                    } else {

                        mtodoListViewModel.updatedetails(
                            Details(
                                detailsId!!,
                                todoId!!,
                                steps[0].details.title!!,
                                detailsdescription,
                                0,
                                detailsdate,
                                donesteps,
                                "$donesteps/${runBlocking { getNumberOfStepsInDetail() }}",
                                progress
                            )
                        )

                        val color = ContextCompat.getColor(context, R.color.orange)
                        _binding!!.stepsiv.setColorFilter(color, PorterDuff.Mode.SRC_IN)

                        isUserUpdate = true
                    }
                }

                _binding!!.titletv.text = steps[0].details.title
                _binding!!.descriptiontv.text = detailsdescription
                _binding!!.datetv.text = detailsdate
                _binding!!.stepstv.text = steps[0].details.steps
                _binding!!.statusSpinner.setSelection(steps[0].details.status)
            }
        }

        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding!!.stepsRv.adapter = stepsAdapter
        _binding!!.stepsRv.layoutManager = LinearLayoutManager(requireContext())
        _binding!!.statusSpinner.isEnabled = false

        adapter= SpinnerAdapter(requireContext(),statusList)
        _binding!!.statusSpinner.adapter=adapter




        stepsAdapter.setOnItemClickListener(object : StepsAdapter.onItemClickListener {

            override fun onItemClick(
                id: Int,
                detailsId: Int,
                todoId: Int,
                stepNum: Int,
                title: String?,
                description: String?,
                status: Int,
            ) {
                mtodoListViewModel.updatesteps(
                    Steps(
                        id,
                        detailsId,
                        todoId,
                        stepNum,
                        title,
                        description,
                        status
                    )
                )
                isUserUpdate = false

            }
        })


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