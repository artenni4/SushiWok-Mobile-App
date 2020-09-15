package com.example.sushiwokmobile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoadingScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading_screen)

        try {
            Firebase.database.setPersistenceEnabled(true)
        }
        catch (e: com.google.firebase.database.DatabaseException)
        { }
        updatePage()
    }

    private fun updatePage() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            val intent = Intent(this, StartLoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, CourierActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}