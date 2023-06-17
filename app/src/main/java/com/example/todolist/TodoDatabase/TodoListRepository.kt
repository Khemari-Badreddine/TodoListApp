package com.example.todolist.TodoDatabase

import androidx.annotation.WorkerThread
import com.example.todolist.DetailsDatabase.Details
import com.example.todolist.Relations.TodoWithDetails
import kotlinx.coroutines.flow.Flow


class todoListRepository(private val todoListdao: todoListDao) {

    val alltodo: Flow<List<Todo>> = todoListdao.getalltodoList()
    fun alldetails(id : Int) : Flow<List<TodoWithDetails>>{
        return todoListdao.getTodoWithDetails(id)
    }


    @WorkerThread
    suspend fun inserttodo(todo: Todo) {
        todoListdao.inserttodo(todo)
    }


    suspend fun updatetodo(todo: Todo) {
        todoListdao.updatetodo(todo)
    }

    suspend fun update(todos: List<Todo>) {
        todoListdao.update(todos)
    }


    suspend fun deletetodo(todo: Todo) {
        todoListdao.deletetodo(todo)
    }


    //Details Repository

    suspend fun insertdetails(details: Details) {
        todoListdao.insertdetails(details)
    }

    suspend fun updatedetails(details: Details) {
        todoListdao.updatedetails(details)
    }

    suspend fun deletedetails(id: Int) {
        todoListdao.deletedetails(id)
    }

    suspend fun deleteTodoWithDetails(id: Int) {
        todoListdao.deleteTodoWithDetails(id)
    }


}