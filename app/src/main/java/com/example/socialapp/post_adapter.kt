package com.example.socialapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialapp.models.post
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class post_adapter(options: FirestoreRecyclerOptions<post>,val listener:iadapter) : FirestoreRecyclerAdapter<post, post_adapter.postviewholder>(
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
        val viewholder=postviewholder(LayoutInflater.from(parent.context).inflate(R.layout.post_item,parent ,false))
        viewholder.likebutton.setOnClickListener {
            listener.onlikeclicked(snapshots.getSnapshot(viewholder.adapterPosition).id)
        }
        return viewholder
    }

    override fun onBindViewHolder(holder: postviewholder, position: Int, model: post) {
       holder.posttext.text=model.text
       holder.usertext.text=model.createdBy.displayname
       Glide.with(holder.userimage.context).load(model.createdBy.imageurl).circleCrop().into(holder.userimage)
       holder.likecount.text=model.likedby.size.toString()
        holder.createdAt.text=Utils.getTimeAgo(model.createdAt)
        val auth= Firebase.auth
        val currentuserid=auth.currentUser?.uid
        val isliked= model?.likedby?.contains(currentuserid)
        if(!isliked){
            holder.likebutton.setImageDrawable(ContextCompat.getDrawable(holder.likebutton.context,R.drawable.ic_baseline_favorite_border_24))
        }else
            holder.likebutton.setImageDrawable(ContextCompat.getDrawable(holder.likebutton.context,R.drawable.ic_baseline_favorite_24))
    }
    interface iadapter{
        fun onlikeclicked(postid:String)

    }
}