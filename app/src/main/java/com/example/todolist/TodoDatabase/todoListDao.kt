package com.example.todolist.TodoDatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.todolist.Database.Details
import com.example.todolist.Database.Steps
import com.example.todolist.Database.Todo
import com.example.todolist.Relations.DetailsWithSteps
import com.example.todolist.Relations.TodoWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface todoListDao {


    //Todo DAO

    @Query("SELECT * FROM todoList_table ORDER BY id ASC")
    fun getalltodoList(): Flow<List<Todo>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun inserttodo(todo: Todo)


    @Update
    fun updatetodo(todo: Todo)

    @Update
    fun update(todos: List<Todo>)


    @Delete
    fun deletetodo(todo: Todo)

    @Query("DELETE FROM todoList_table")
    fun deleteAll()


    //===========Details DAO============
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertdetails(details: Details)

    @Transaction
    @Query("SELECT * FROM todoList_table WHERE id =:todoId")
    fun getTodoWithDetails(todoId: Int): Flow<List<TodoWithDetails>>

    @Update
    fun updatedetails(details: Details)


    @Transaction
    @Query("DELETE FROM details_table WHERE id =:id")
    fun deletedetails(id: Int)

    @Transaction
    @Query("DELETE FROM details_table WHERE todoId =:id")
    fun deleteTodoWithDetails(id: Int)

    @Query("SELECT MAX(id) + 1 FROM Details_table")
    fun getNextDetailsId(): Int


    //===========Steps DAO============

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertsteps(steps: Steps)

    @Transaction
    @Query("SELECT * FROM details_table WHERE id =:detailsId")
    fun getDetailsWithSteps(detailsId: Int): Flow<List<DetailsWithSteps>>

    @Query("UPDATE Steps_table SET col_title = :title WHERE id = :id")
    fun updatesteps(id: Int,title: String?)

    @Query("UPDATE Steps_table SET stepNum = :newStepNum WHERE id = :stepsId")
    fun updateStepNum(stepsId: Int,newStepNum: Int)

    @Transaction
    @Query("DELETE FROM Steps_table WHERE detailsId =:id")
    fun deletesteps(id: Int)

    @Transaction
    @Query("DELETE FROM Steps_table WHERE todoId =:id")
    fun deleteTodoWithSteps(id: Int)


    @Transaction
    @Query("DELETE FROM Steps_table")
    fun deleteallsteps()

}
