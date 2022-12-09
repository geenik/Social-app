package com.example.socialapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.socialapp.daos.postdao

class createpostactivity : AppCompatActivity() {
    private lateinit var postdao:postdao
    lateinit var pstbtn:Button
    lateinit var textfield:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_createpostactivity)
        postdao= postdao()
        pstbtn=findViewById(R.id.postbtn)
        textfield=findViewById(R.id.textfield)
        pstbtn.setOnClickListener {
            val input=textfield.text.toString().trim()
            if(input.isNotEmpty()){
                postdao.addpost(input)
                finish()
            }
        }
    }
}