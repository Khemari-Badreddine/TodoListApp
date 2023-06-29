package com.example.todolist.TodoDatabase

import androidx.annotation.WorkerThread
import com.example.todolist.Database.Details
import com.example.todolist.Database.Steps
import com.example.todolist.Database.Todo
import com.example.todolist.Relations.DetailsWithSteps
import com.example.todolist.Relations.TodoWithDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext


class todoListRepository(private val todoListdao: todoListDao) {

    val alltodo: Flow<List<Todo>> = todoListdao.getalltodoList()
    fun alldetails(id: Int): Flow<List<TodoWithDetails>> {
        return todoListdao.getTodoWithDetails(id)
    }

    fun DetailsWithSteps(id: Int): Flow<List<DetailsWithSteps>> {
        return todoListdao.getDetailsWithSteps(id)
    }
    

    @WorkerThread
    fun inserttodo(todo: Todo) {
        todoListdao.inserttodo(todo)
    }


    fun updatetodo(todo: Todo) {
        todoListdao.updatetodo(todo)
    }

    fun update(todos: List<Todo>) {
        todoListdao.update(todos)
    }


     fun deletetodo(todo: Todo) {
        todoListdao.deletetodo(todo)
    }


    //Details Repository

     fun insertdetails(details: Details) {
        todoListdao.insertdetails(details)
    }

     fun updatedetails(details: Details) {
        todoListdao.updatedetails(details)
    }

     fun deletedetails(id: Int) {
        todoListdao.deletedetails(id)
    }

     fun deleteTodoWithDetails(id: Int) {
        todoListdao.deleteTodoWithDetails(id)
    }

    suspend fun getNextDetailsId(): Int = withContext(Dispatchers.IO) {
        todoListdao.getNextDetailsId()
    }


    //Steps Repository

     fun insertsteps(steps: Steps) {
        todoListdao.insertsteps(steps)
    }

     fun updatesteps(id: Int, title: String?) {
        todoListdao.updatesteps(id, title)
    }

     fun updateStepNum(stepsId: Int, newStepNum: Int) {
        todoListdao.updateStepNum(stepsId, newStepNum)
    }

     fun deletesteps(id: Int) {
        todoListdao.deletesteps(id)
    }

     fun deleteTodoWithSteps(id: Int) {
        todoListdao.deleteTodoWithSteps(id)
    }

    fun deleteallsteps() {
        todoListdao.deleteallsteps()
    }


}