package com.example.snapchatclone

import android.R.attr.password
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    var emailEditText : EditText? = null;
    var passEditText : EditText? = null;

    val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emailEditText = findViewById(R.id.userNameEditText)
        passEditText = findViewById(R.id.passEditText)

        if(mAuth.currentUser != null) {
            logIn()
        }
    }

    fun goClicked(view: View) {
        // check if user exists
        mAuth.signInWithEmailAndPassword(emailEditText?.text.toString(), passEditText?.text.toString())
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    logIn()
                } else {
                    // If sign in fails create the user
                    mAuth.createUserWithEmailAndPassword(emailEditText?.text.toString(), passEditText?.text.toString()).addOnCompleteListener(this){ task ->
                        if (task.isSuccessful) {
                            //add to database
                            var userID = task.result?.user!!.uid
                            Toast.makeText(this,userID,Toast.LENGTH_SHORT).show()
                            lateinit var database: DatabaseReference
                            database = Firebase.database.reference
                            database.child("users").child(userID).child("email").setValue(emailEditText?.text.toString())
                            logIn()
                        } else {
                            Toast.makeText(this,"Login Failed. Try Again.",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
    }

    fun logIn() {
        // move to new activity
        val intent = Intent(this,SnapsActivity::class.java)
        startActivity(intent)
    }
}
