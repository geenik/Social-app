package com.example.socialapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import com.example.socialapp.daos.userdao
import com.example.socialapp.models.user
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.math.sign

class sign_in_activity : AppCompatActivity() {
    private val RC_SIGN_IN=123
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    lateinit var signinbtn:SignInButton
    lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        progressBar=findViewById(R.id.progressbar)
         signinbtn=findViewById(R.id.signinbtn)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth=Firebase.auth
        signinbtn.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSigninResult(task)
        }
    }

    override fun onStart() {
        val currentuser=auth.currentUser
        updateui(currentuser)
        super.onStart()
    }
    private fun handleSigninResult(task: Task<GoogleSignInAccount>?) {
        try {
            // Google Sign In was successful, authenticate with Firebase
            val account = task?.getResult(ApiException::class.java)!!
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            // Google Sign In failed, update UI appropriately

        }

    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        signinbtn.visibility=View.GONE
        progressBar.visibility=View.VISIBLE
       GlobalScope.launch(Dispatchers.IO) {
           val auth=auth.signInWithCredential(credential).await()
           val firebaseuser= auth.user
           withContext(Dispatchers.Main){
               updateui(firebaseuser)
           }
       }
    }

    private fun updateui(firebaseuser: FirebaseUser?) {
      if(firebaseuser!=null){
          val user= user(firebaseuser.uid,firebaseuser.displayName as String,firebaseuser.photoUrl.toString())
          val dao=userdao()
          dao.adduser(user)
          startActivity(Intent(this,MainActivity::class.java))
          finish()
      }else{
          signinbtn.visibility=View.VISIBLE
          progressBar.visibility=View.GONE
      }
    }
}