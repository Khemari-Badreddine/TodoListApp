package com.example.todolist.database

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow


class todoListRepository(private val todoListdao: todoListDao) {

    val alltodo: Flow<List<Todo>> = todoListdao.getalltodoList()

    @WorkerThread
    suspend fun inserttodo(todo: Todo) {
        todoListdao.inserttodo(todo)
    }

    suspend fun deletetodo(todo: Todo) {
        todoListdao.deletetodo(todo)
    }
}