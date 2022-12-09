package com.example.socialapp.daos

import com.example.socialapp.models.user
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class userdao {
    val usercollection= FirebaseFirestore.getInstance().collection("User")
    fun adduser(user: user){
        user?.let {
            GlobalScope.launch(Dispatchers.IO)
            {
                usercollection.document(user.uid).set(it)
            }
        }
    }
    fun getuserbyid(uid:String): Task<DocumentSnapshot> {
        return usercollection.document(uid).get()
    }
}