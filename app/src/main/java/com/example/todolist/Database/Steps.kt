package com.example.todolist.Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("Steps_table")
data class Steps (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "detailsId") val detailsId: Int,
    @ColumnInfo(name = "todoId") val todoId: Int,
    @ColumnInfo(name = "stepNum") var stepNum: Int,
    @ColumnInfo(name="col_title")var title: String?,
    @ColumnInfo(name="col_description") var description: String?,
    @ColumnInfo(name="col_status")val status: Int,
)
