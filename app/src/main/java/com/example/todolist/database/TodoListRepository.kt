package com.example.todolist.database

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData


class todoListRepository(private val todoListdao: todoListDao) {

    val alltodo: LiveData<List<todo>> = todoListdao.getalltodoList()

    @WorkerThread
    suspend fun inserttodo(todo: todo) {
        todoListdao.inserttodo(todo)
    }

    suspend fun deletetodo(todo: todo) {
        todoListdao.deletetodo(todo)
    }
}