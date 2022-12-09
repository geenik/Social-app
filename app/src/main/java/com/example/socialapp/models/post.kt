package com.example.socialapp.models

data class post(
    val text:String="",
    val createdBy:user=user(),
    val createdAt:Long=0L,
    val likedby:ArrayList<String> =ArrayList()
        )