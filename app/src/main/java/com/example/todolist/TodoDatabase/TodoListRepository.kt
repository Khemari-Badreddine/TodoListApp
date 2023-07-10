package com.example.todolist.TodoDatabase

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
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

    fun searchtodoListtable(searchQuery: String): LiveData<List<Todo>> {
        return todoListdao.searchtodoListtable(searchQuery).asLiveData()
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

    fun searchtdetailstable(searchQuery: String,id: Int): LiveData<List<Details>> {
        return todoListdao.searchtdetailstable(searchQuery,id).asLiveData()
    }




    //Steps Repository

     fun insertsteps(steps: Steps) {
        todoListdao.insertsteps(steps)
    }


     fun updatesteps(steps: Steps) {
        todoListdao.updatesteps(steps)
    }


     fun deleteTodoWithSteps(id: Int) {
        todoListdao.deleteTodoWithSteps(id)
    }

    suspend fun getNumberOfStepsWithStatus2(id: Int): Int = withContext(Dispatchers.IO) {
        todoListdao.getNumberOfStepsWithStatus2(id)
    }

    suspend fun getNumberOfStepsInDetail(id: Int): Int = withContext(Dispatchers.IO) {
        todoListdao.getNumberOfStepsInDetail(id)
    }

    suspend fun getMaxstepNum(id: Int): Int = withContext(Dispatchers.IO) {
        todoListdao.getMaxstepNum(id)
    }

    fun deletestepswithid(idtodelete: Int) {
        todoListdao.deletestepswithid(idtodelete)
    }


    fun deleteAndCreate(steps: MutableList<Steps>,id: Int) {
        todoListdao.deleteAndCreate(steps,id)
    }




}