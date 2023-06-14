package com.example.todolist.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = [todo::class], version = 1, exportSchema = false)
abstract class todoListDataBase : RoomDatabase() {

    abstract fun getDao(): todoListDao


    companion object {
        @Volatile
        private var INSTANCE: todoListDataBase? = null

        @OptIn(InternalCoroutinesApi::class)
        fun getDatabase(context: Context): todoListDataBase {
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    todoListDataBase::class.java,
                    "note_db"

                ).build()
                INSTANCE = instance
                instance
            }


        }
    }
}