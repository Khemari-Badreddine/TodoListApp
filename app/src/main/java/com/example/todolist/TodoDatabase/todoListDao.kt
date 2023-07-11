package com.example.todolist.TodoDatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
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

    @Query("SELECT * FROM todoList_table WHERE col_title LIKE :searchQuery ")
    fun searchtodoListtable(searchQuery: String): Flow<List<Todo>>



    //===========Details DAO============
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertdetails(details: Details)

    @Transaction
    @Query("SELECT * FROM todoList_table WHERE id =:todoId ORDER BY id ASC")
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

    @Query("UPDATE Details_table SET col_status = :status WHERE id = :id")
    fun updatestatus(id: Int, status: Int)

    @Query("SELECT * FROM details_table WHERE col_title LIKE :searchQuery AND todoId = :id")
    fun searchtdetailstable(searchQuery: String,id: Int): Flow<List<Details>>


    //===========Steps DAO============


    @Upsert
    fun insertsteps(steps: Steps)

    @Transaction
    @Query("SELECT * FROM details_table WHERE id =:detailsId ORDER BY id ASC")
    fun getDetailsWithSteps(detailsId: Int): Flow<List<DetailsWithSteps>>

    @Insert
    fun insertAllsteps(steps: MutableList<Steps>)

    @Update
    fun updatesteps(steps: Steps)

    @Delete
    fun deletesteps(steps: Steps)

    @Query("DELETE FROM Steps_table WHERE detailsId =:idtodelete")
    fun deletestepswithid(idtodelete: Int)

    @Transaction
    fun deleteAndCreate(steps: MutableList<Steps>, id: Int) {
        deletestepswithid(id)
        insertAllsteps(steps)
    }

    @Transaction
    @Query("DELETE FROM Steps_table WHERE todoId =:id")
    fun deleteTodoWithSteps(id: Int)

    @Query("SELECT COUNT(*) FROM Steps_table WHERE col_status = 2 AND detailsId = :id")
    fun getNumberOfStepsWithStatus2(id: Int): Int

    @Query("SELECT COUNT(*) FROM Steps_table WHERE detailsId =:idtoseek")
    fun getNumberOfStepsInDetail(idtoseek: Int): Int

    @Query("SELECT MAX(stepNum) + 1 FROM Steps_table where detailsId = :id")
    fun getMaxstepNum(id: Int): Int

}
