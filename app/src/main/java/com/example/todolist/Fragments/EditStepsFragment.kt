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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.Database.Details
import com.example.todolist.Database.Steps
import com.example.todolist.HelperClasses.CreateTaskStepsAdapter
import com.example.todolist.R
import com.example.todolist.TodoDatabase.todoListApplication
import com.example.todolist.TodoDatabase.todoListViewModel
import com.example.todolist.databinding.FragmentEditStepsBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Calendar


class editStepsFragment : Fragment() {


    private var _binding: FragmentEditStepsBinding? = null
    private var newList = mutableListOf<Steps>()

    var detailsId: Int? = 0
    var todoId: Int? = 0

    private var k: Int = 1
    private lateinit var context: Context
    private var isUserUpdate = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
    }

    private val mtodoListViewModel: todoListViewModel by viewModels {
        todoListViewModel.todoListViewModelFactory((requireContext().applicationContext as todoListApplication).repository)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailsId = arguments?.getInt("detailsId")
        todoId = arguments?.getInt("todoId")
        if (runBlocking { getMaxstepNum() } != 0) k = runBlocking { getMaxstepNum() }
    }

    private suspend fun getMaxstepNum(): Int {
        return mtodoListViewModel.getMaxstepNum(detailsId!!)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_steps, container, false)
        _binding = FragmentEditStepsBinding.bind(view)

        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val stepsAdapter = CreateTaskStepsAdapter(requireContext())
        _binding!!.stepsRv.adapter = stepsAdapter
        _binding!!.stepsRv.layoutManager = LinearLayoutManager(requireContext())

        mtodoListViewModel.DetailsWithSteps(detailsId!!).observe(viewLifecycleOwner) { steps ->
            steps?.let {
                newList = steps[0].steps.toMutableList()
                stepsAdapter.setData(newList)
                _binding!!.titleEt.setText(steps[0].details.title)
                _binding!!.descriptionEt.setText(steps[0].details.description)
                _binding!!.dateEt.setText(steps[0].details.date)
            }

        }

        _binding!!.addStepiv.setOnClickListener {

            if (k <= 5) {

                Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_SHORT).show()
                newList.add(Steps(0, detailsId!!, todoId!!, k, null, null, 0))
                k++
                stepsAdapter.setData(newList)
            } else {
                Toast.makeText(requireContext(), "You can't add more steps", Toast.LENGTH_LONG)
                    .show()
            }
        }
        _binding!!.canceltask.setOnClickListener {
            requireActivity().onBackPressed()
        }
        _binding!!.dateEt.setOnClickListener { showDatePickerDialog() }

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
                    isUserUpdate = false

                    if (k > 1) k--

                } else if (indicator == 2) {

                    if (!newString.equals("")) newList[adapterposition].title = newString

                } else if (indicator == 3) {

                    if (!newString.equals("")) newList[adapterposition].description = newString

                }

            }
        })

        _binding!!.checkFAB.setOnClickListener {
            insertDataToDatabase()
        }

    }


    private fun insertDataToDatabase() {

        val title = _binding!!.titleEt.text.toString()
        val description = _binding!!.descriptionEt.text.toString()
        val date = _binding!!.dateEt.text.toString()

        if (inputCheck(title, date)) {
            val detail = Details(detailsId!!, todoId!!, title, description, 0, date, "0", "",0)
            mtodoListViewModel.updatedetails(detail)

            mtodoListViewModel.deleteAndCreate(newList,detailsId!!)

            Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_SHORT).show()

            val bundle = Bundle()
            bundle.putInt("todoId", todoId!!)
            bundle.putInt("detailsId", detailsId!!)

            findNavController().popBackStack()
            findNavController().navigate(
                R.id.stepsFragment,
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
