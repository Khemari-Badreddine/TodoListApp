package com.example.todolist.Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("Details_table")
data class Details(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "todoId") val todoId: Int,
    @ColumnInfo(name="col_title")val title: String,
    @ColumnInfo(name="col_description")val description: String,
    @ColumnInfo(name="col_status")val status: Int,
    @ColumnInfo(name="col_date")val date: String,
    @ColumnInfo(name="col_steps")val steps: String

)