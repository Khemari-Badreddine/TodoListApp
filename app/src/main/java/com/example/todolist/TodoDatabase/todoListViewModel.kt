package com.example.todolist.TodoDatabase

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.todolist.Database.Details
import com.example.todolist.Database.Steps
import com.example.todolist.Database.Todo
import com.example.todolist.Relations.DetailsWithSteps
import com.example.todolist.Relations.TodoWithDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class todoListViewModel(
    private val todoRepository: todoListRepository,
) : ViewModel() {

    val alltodo: LiveData<List<Todo>> = todoRepository.alltodo.asLiveData()

    fun alldetails(id: Int): LiveData<List<TodoWithDetails>> {
        return todoRepository.alldetails(id).asLiveData()
    }

    fun DetailsWithSteps(id: Int): LiveData<List<DetailsWithSteps>> {
        return todoRepository.DetailsWithSteps(id).asLiveData()
    }


    fun addtodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            todoRepository.inserttodo(todo)

        }
    }

    fun updatetodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            todoRepository.updatetodo(todo)

        }
    }

    fun searchtodoListtable(searchQuery: String): LiveData<List<Todo>> {
            return todoRepository.searchtodoListtable(searchQuery)
    }


    fun deletetodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            todoRepository.deletetodo(todo)

        }
    }

    //Details ViewModel

    fun adddetails(details: Details) {
        viewModelScope.launch(Dispatchers.IO) {
            todoRepository.insertdetails(details)

        }
    }


    fun updatedetails(details: Details) {
        viewModelScope.launch(Dispatchers.IO) {
            todoRepository.updatedetails(details)

        }
    }


    fun deletedetails(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            todoRepository.deletedetails(id)

        }
    }

    fun deleteTodoWithDetails(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            todoRepository.deleteTodoWithDetails(id)

        }
    }

    suspend fun getNextDetailsId(): Int {
        return todoRepository.getNextDetailsId()
    }

    fun searchtdetailstable(searchQuery: String,id: Int): LiveData<List<Details>> {
        return todoRepository.searchtdetailstable(searchQuery,id)
    }


    //Steps ViewModel

     fun insertsteps(steps: Steps) {
        viewModelScope.launch(Dispatchers.IO) {
            todoRepository.insertsteps(steps)

        }
    }


    fun updatesteps(steps: Steps) {
        viewModelScope.launch(Dispatchers.IO) {
            todoRepository.updatesteps(steps)

        }
    }


    fun deleteTodoWithSteps(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            todoRepository.deleteTodoWithSteps(id)

        }
    }

     fun deletestepswithid(idtodelete: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            todoRepository.deletestepswithid(idtodelete)

        }
    }

    fun deleteAndCreate(steps: MutableList<Steps>,id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            todoRepository.deleteAndCreate(steps,id)

        }
    }


    suspend fun getNumberOfStepsWithStatus2(id: Int): Int {
        return todoRepository.getNumberOfStepsWithStatus2(id)
    }

    suspend fun getNumberOfStepsInDetail(id: Int): Int {
        return todoRepository.getNumberOfStepsInDetail(id)
    }


    suspend fun getMaxstepNum(id: Int): Int {
        return todoRepository.getMaxstepNum(id)
    }


    class todoListViewModelFactory(private val repository: todoListRepository) :
        ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            if (modelClass.isAssignableFrom(todoListViewModel::class.java)) {

                return todoListViewModel(repository) as T
            }
            throw IllegalArgumentException("UKNOWN VIEWMODEL CLASS")

        }
    }
}