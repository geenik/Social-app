package com.example.socialapp.daos

import android.text.BoringLayout
import com.example.socialapp.models.post
import com.example.socialapp.models.user
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
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
    fun getpostbyid(postid:String): Task<DocumentSnapshot> {
        return postcolllections.document(postid).get()
    }
    fun updatelikes(postid: String){
        val currentuserid=auth.currentUser?.uid as String
        GlobalScope.launch {
            val post=getpostbyid(postid).await().toObject(post::class.java)
            val isliked= post?.likedby?.contains(currentuserid) as Boolean
            if(isliked){
                post.likedby.remove(currentuserid)
            }else post.likedby.add(currentuserid)
            postcolllections.document(postid).set(post)
        }


    }
}