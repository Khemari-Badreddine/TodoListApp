package com.example.todolist.database

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


interface todoListDao {


    @Query("SELECT * FROM todoList_table ORDER BY id ASC")
    fun getalltodoList() : LiveData<List<todo>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun inserttodo(todo: todo)

    @Delete
    suspend fun deletetodo(todo: todo)

}
