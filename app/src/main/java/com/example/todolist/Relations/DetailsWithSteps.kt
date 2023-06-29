package com.example.todolist.Relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.todolist.Database.Details
import com.example.todolist.Database.Steps
import com.example.todolist.Database.Todo

data class DetailsWithSteps (
    @Embedded
    val details: Details,
    @Relation(
        parentColumn = "id",
        entityColumn = "detailsId"
    )
    val steps: List<Steps>
)
