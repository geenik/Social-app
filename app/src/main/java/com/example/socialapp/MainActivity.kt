package com.example.socialapp

import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialapp.daos.postdao
import com.example.socialapp.models.post
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import android.content.DialogInterface





class MainActivity : AppCompatActivity(),post_adapter.iadapter {
    private lateinit var adapter:post_adapter
    lateinit var recyclerView: RecyclerView
    lateinit var postdao: postdao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val addbtn:FloatingActionButton=findViewById(R.id.addbtn)
        recyclerView=findViewById(R.id.recyclerview)
        addbtn.setOnClickListener{
         val intent=Intent(this,createpostactivity::class.java)
            startActivity(intent)
        }
        setuprecycler()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_logout, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.logout){
            logoutpermission();
        }
        return super.onOptionsItemSelected(item)
    }
    fun setuprecycler(){
        postdao=postdao()
        val postcollection=postdao.postcolllections
        val query=postcollection.orderBy("createdAt",Query.Direction.DESCENDING)
        val recyclerViewoption=FirestoreRecyclerOptions.Builder<post>().setQuery(query,post::class.java).build()
        adapter= post_adapter(recyclerViewoption,this)
        recyclerView.adapter=adapter
        recyclerView.layoutManager=LinearLayoutManager(this)
    }

    override fun onStart() {
        adapter.startListening()
        super.onStart()
    }

    override fun onStop() {
        adapter.stopListening()
        super.onStop()
    }

    override fun onlikeclicked(postid: String) {
        postdao.updatelikes(postid)
    }
    fun logout(){
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this,sign_in_activity::class.java))
        finish()
    }
     fun logoutpermission() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes"
            ) { dialog, which -> logout() }
            .setNegativeButton("No", null)
            .show()
    }
}