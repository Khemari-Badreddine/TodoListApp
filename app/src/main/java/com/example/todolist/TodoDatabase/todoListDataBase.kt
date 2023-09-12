package com.example.todolist.TodoDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todolist.Database.Details
import com.example.todolist.Database.Steps
import com.example.todolist.Database.Todo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import kotlinx.coroutines.launch

@Database(
    entities = [Todo::class,Details::class,Steps::class],
    version = 1,
    exportSchema = false
)
abstract class todoListDataBase : RoomDatabase() {

    abstract fun getDao(): todoListDao

    private class todoDatabaseCallback(private val scope: CoroutineScope) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { db ->
                scope.launch {
                    populateDatabase(db.getDao())
                }
            }
        }

        //use this function to populate the database if needed
        fun populateDatabase(todoDao: todoListDao) {
            todoDao.deleteAll()

            // val todo = Todo(0, "lmfao", 1)
            //  todoDao.inserttodo(todo)

        }
    }

    companion object {
        @Volatile
        private var INSTANCE: todoListDataBase? = null

        @OptIn(InternalCoroutinesApi::class)
        fun getDatabase(context: Context, scope: CoroutineScope): todoListDataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    todoListDataBase::class.java,
                    "note_db"
                )
                    .addCallback(todoDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }


        }
    }
}