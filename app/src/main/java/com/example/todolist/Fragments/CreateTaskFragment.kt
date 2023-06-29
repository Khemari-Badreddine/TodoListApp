package com.example.todolist.Fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.Database.Details
import com.example.todolist.Database.Steps
import com.example.todolist.Database.Todo
import com.example.todolist.HelperClasses.CreateTaskStepsAdapter
import com.example.todolist.HelperClasses.StepsAdapter
import com.example.todolist.HelperClasses.todoListAdapter
import com.example.todolist.R
import com.example.todolist.Relations.DetailsWithSteps
import com.example.todolist.TodoDatabase.todoListApplication
import com.example.todolist.TodoDatabase.todoListViewModel
import com.example.todolist.databinding.FragmentCreateTaskBinding
import kotlinx.coroutines.runBlocking
import java.util.Calendar

class createTaskFragment : Fragment() {


    private var _binding: FragmentCreateTaskBinding? = null
    var heading: String? = null
    private var newList = mutableListOf<Steps>()

    var todoId: Int? = 0

    private val mtodoListViewModel: todoListViewModel by viewModels {
        todoListViewModel.todoListViewModelFactory((requireContext().applicationContext as todoListApplication).repository)
    }


    private var detailsId: Int = 0
    private var k: Int = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        heading = arguments?.getString("heading")
        todoId = arguments?.getInt("todoId")
        detailsId = arguments?.getInt("detailsId")!!

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_create_task, container, false)
        _binding = FragmentCreateTaskBinding.bind(view)

        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding!!.dateEt.setOnClickListener { showDatePickerDialog() }
        newList.add(Steps(0, detailsId, todoId!!, k, null, null, 0))

        val stepsAdapter = CreateTaskStepsAdapter(requireContext())
        _binding!!.stepsRv.adapter = stepsAdapter
        _binding!!.stepsRv.layoutManager = LinearLayoutManager(requireContext())

        stepsAdapter.setData(newList)


        _binding!!.addStepiv.setOnClickListener {

            if (k < 5) {
                k++
                Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_SHORT).show()

                val newStep = Steps(0, detailsId, todoId!!, k, null, null, 0)
                newList.add(newStep)
                stepsAdapter.setData(newList)
            } else {
                Toast.makeText(requireContext(), "You can't add more steps", Toast.LENGTH_LONG)
                    .show()
            }
        }

        _binding!!.canceltask.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("heading", heading)
            bundle.putInt("todoId", todoId!!)
            mtodoListViewModel.deletesteps(detailsId)
            findNavController().navigate(
                R.id.detailsFragment,
                bundle
            )
        }

        _binding!!.checkFAB.setOnClickListener {
            insertDataToDatabase()

        }

        stepsAdapter.setOnItemClickListener(object : CreateTaskStepsAdapter.onItemClickListener {

            override fun onItemClick(
                step: Steps,
                steps: MutableList<Steps>,
                newString: String,
                adapterposition: Int,
                indicator: Int
            ) {

                if (indicator == 1) {
                    steps.removeAt(adapterposition)

                    for (index in steps.indices) {
                        steps[index].stepNum = index + 1
                    }

                    newList = steps
                    stepsAdapter.setData(newList)

                    k--
                } else if (indicator == 2) {

                    if (!newString.equals("")) newList[adapterposition].title = newString

                } else if (indicator == 3) {

                    if (!newString.equals("")) newList[adapterposition].description = newString

                }

            }
        })

    }

    private fun insertDataToDatabase() {

        val title = _binding!!.titleEt.text.toString()
        val description = _binding!!.descriptionEt.text.toString()
        val date = _binding!!.dateEt.text.toString()

        if (inputCheck(title, date)) {
            val detail = Details(0, todoId!!, title, description, 0, date, "1/" + k.toString())

            for (step in newList) {
                mtodoListViewModel.addsteps(step)
            }

            mtodoListViewModel.adddetails(detail)

            Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_SHORT).show()

            val bundle = Bundle()
            bundle.putString("heading", heading)
            bundle.putInt("todoId", todoId!!)

            findNavController().navigate(
                R.id.detailsFragment,
                bundle
            )

        } else {
            Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_LONG)
                .show()
        }


    }

    private fun inputCheck(title: String, date: String): Boolean {
        return !(TextUtils.isEmpty(title) || TextUtils.isEmpty(date))
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]

        val datePickerDialog = DatePickerDialog(
            ContextThemeWrapper(
                context,
                R.style.BlueDatePickerDialogTheme
            ),
            { view, year, monthOfYear, dayOfMonth ->
                _binding!!.dateEt.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year)
            }, year, month, day
        )
        datePickerDialog.show()
    }


}