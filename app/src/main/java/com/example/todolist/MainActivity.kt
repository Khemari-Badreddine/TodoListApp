package com.example.todolist


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

}

annotation class AndroidEntryPoint
