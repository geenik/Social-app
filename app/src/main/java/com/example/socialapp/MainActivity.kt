package com.example.socialapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val addbtn:FloatingActionButton=findViewById(R.id.addbtn)
        addbtn.setOnClickListener{
         val intent=Intent(this,createpostactivity::class.java)
            startActivity(intent)
        }
    }
}