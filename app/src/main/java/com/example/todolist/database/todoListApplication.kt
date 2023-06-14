package com.example.todolist.database

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class todoListApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())



    val database by lazy { todoListDataBase.getDatabase(this,applicationScope)  }
    val repository by lazy { todoListRepository(database.getDao()) }

}