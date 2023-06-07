package com.example.todolist.database

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

interface todoListDao {


    @Query("SELECT * FROM todoList_table")
    fun getalltodoList() : LiveData<List<todoList>>

    @Insert
    suspend fun addNote(todo: todoList)

    @Delete
    suspend fun deleteNote(todo: todoList)

}
