package com.example.todolist.Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity("todoList_table")
data class Todo(
    @PrimaryKey (autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name="col_title")val title: String,
    @ColumnInfo(name="col_status")val status: Int
)


