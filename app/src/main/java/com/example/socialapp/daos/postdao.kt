package com.example.socialapp.daos

import com.example.socialapp.models.post
import com.example.socialapp.models.user
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class postdao {
    val postcolllections=FirebaseFirestore.getInstance().collection("post")
    val auth=Firebase.auth

    fun addpost(text:String){
        val currentuserid= auth.currentUser?.uid
        val userdao=userdao()
        GlobalScope.launch(Dispatchers.IO) {
            val user= currentuserid?.let { userdao.getuserbyid(it).await().toObject(user::class.java) }
            val currenttime=System.currentTimeMillis()
            val post=post(text,user as user,currenttime)
            postcolllections.document().set(post)
        }

    }
}