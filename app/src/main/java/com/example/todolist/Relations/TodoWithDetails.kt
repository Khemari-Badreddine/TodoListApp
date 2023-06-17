package com.example.todolist.Relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.todolist.DetailsDatabase.Details
import com.example.todolist.TodoDatabase.Todo

data class TodoWithDetails(

    @Embedded val todo: Todo,
    @Relation(
        parentColumn = "id",
        entityColumn = "todoId"
    )
    val details: List<Details>
)
