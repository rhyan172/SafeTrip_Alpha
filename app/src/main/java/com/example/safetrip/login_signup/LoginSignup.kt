package com.example.safetrip

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.safetrip.R.id.signup_button
import com.example.safetrip.R.id.textLogin
import com.example.safetrip.LogInMain
import com.example.safetrip.LogInWelcome

class LoginSignup : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        sharedPreferences = getSharedPreferences("ONE_TIME_ACTIVITY", Context.MODE_PRIVATE)

        val check = sharedPreferences.getBoolean("ONE_TIME", false)
        if(check == true){
            startActivity(Intent(this, LogInWelcome::class.java))
            finish()
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_signup)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        val button = findViewById<Button>(signup_button)
        button.setOnClickListener {
            val intent = Intent(this, SignUpNumber::class.java)
            startActivity(intent)
        }

        val textView = findViewById<TextView>(textLogin)
        textView.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.putBoolean("ONE_TIME", true)
            editor.apply()
            val intent = Intent(this, LogInMain::class.java)
            startActivity(intent)
        }
    }

    private fun CheckUser(){

    }
}