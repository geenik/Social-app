package com.example.socialapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialapp.models.post
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class post_adapter(options: FirestoreRecyclerOptions<post>) : FirestoreRecyclerAdapter<post, post_adapter.postviewholder>(
    options
) {
    class postviewholder(itemview:View):RecyclerView.ViewHolder(itemview){
        val posttext:TextView=itemview.findViewById(R.id.postTitle)
        val usertext:TextView=itemview.findViewById(R.id.userName)
        val createdAt:TextView=itemview.findViewById(R.id.createdAt)
        val likecount:TextView=itemview.findViewById(R.id.likeCount)
        val userimage:ImageView=itemview.findViewById(R.id.userImage)
        val likebutton:ImageView=itemview.findViewById(R.id.likeButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): postviewholder {
        return postviewholder(LayoutInflater.from(parent.context).inflate(R.layout.post_item,parent ,false))
    }

    override fun onBindViewHolder(holder: postviewholder, position: Int, model: post) {
       holder.posttext.text=model.text
       holder.usertext.text=model.createdBy.displayname
       Glide.with(holder.userimage.context).load(model.createdBy.imageurl).circleCrop().into(holder.userimage)
       holder.likecount.text=model.likedby.size.toString()
        holder.createdAt.text=Utils.getTimeAgo(model.createdAt)
    }
}