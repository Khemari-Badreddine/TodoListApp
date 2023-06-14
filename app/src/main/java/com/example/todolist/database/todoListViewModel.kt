package com.example.todolist.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class todoListViewModel(private val todoRepository: todoListRepository) : ViewModel() {

    val alltodo: LiveData<List<Todo>> = todoRepository.alltodo.asLiveData()



    fun addtodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            todoRepository.inserttodo(todo)

        }
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