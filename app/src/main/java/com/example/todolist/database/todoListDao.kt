package com.example.todolist.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface todoListDao {


    @Query("SELECT * FROM todoList_table ORDER BY id ASC")
    fun getalltodoList(): Flow<List<Todo>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun inserttodo(todo: Todo)

    @Delete
    fun deletetodo(todo: Todo)

    @Query("DELETE FROM todoList_table")
    fun deleteAll()


}
