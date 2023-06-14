package com.example.todolist.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class todoListViewModel(application: Application) : AndroidViewModel(application) {

    val alltodo: LiveData<List<todo>>
    private val todoRepository: todoListRepository


     init {
        val todoDao = todoListDataBase.getDatabase(application).getDao()
        todoRepository = todoListRepository(todoDao)
        alltodo = todoRepository.alltodo

    }


    fun addtodo(todo: todo) {
        viewModelScope.launch(Dispatchers.IO) {
            todoRepository.inserttodo(todo)

        }
    }

   /* class todoListViewModelFactory(private val repository: todoListRepository) :
        ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            if (modelClass.isAssignableFrom(todoListViewModel::class.java)) {

                return todoListViewModel(repository) as T
            }
            throw IllegalArgumentException("UKNOWN VIEWMODEL CLASS")

        }
    }*/
}