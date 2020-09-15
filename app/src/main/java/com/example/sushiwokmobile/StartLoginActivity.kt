package com.example.sushiwokmobile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_start_login.*
import java.io.*
import java.net.InetSocketAddress
import java.net.Socket
import java.util.*
import kotlin.concurrent.thread
import kotlin.math.log


class StartLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.println(Log.DEBUG, null, "start login activity")
        setContentView(R.layout.activity_start_login)

        val loginButton = findViewById<Button>(R.id.btnLogin)

        loginButton.setOnClickListener {
            val loading = LoadingDialog(this)

            loginButton.isEnabled = false
            val originalColor = loginButton.textColors
            loginButton.setTextColor(resources.getColor(R.color.colorInactivity))

            //get all fields
            val loginField = findViewById<EditText>(R.id.etLoginID)
            val passwordField = findViewById<EditText>(R.id.etPassword)
            var btnOriginalColor = btnLogin.textColors
            val email = loginField.text.toString()
            val password = passwordField.text.toString()
            //check if fields are empty and show error if are
            if(email.isEmpty()) {
                loginField.error = "Введите ID курьера"
                loginField.requestFocus()
                loginButton.isEnabled = true
                loginButton.setTextColor(originalColor)
                return@setOnClickListener
            }
            if(password.isEmpty()) {
                passwordField.error = "Введите пароль"
                loginField.requestFocus()
                loginButton.isEnabled = true
                loginButton.setTextColor(originalColor)
                return@setOnClickListener
            }
            loading.startLoadingDialog()
            val auth = FirebaseAuth.getInstance()
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    val intent = Intent(this, CourierActivity::class.java)
                    startActivity(intent)
                    loading.dismissDialog()
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Неправильные данные", Toast.LENGTH_SHORT).show()
                    loginButton.isEnabled = true
                    loginButton.setTextColor(originalColor)
                    loading.dismissDialog()
                }
            }
        }
    }

}