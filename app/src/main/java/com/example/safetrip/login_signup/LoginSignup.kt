package com.example.safetrip

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.safetrip.R.id.signup_button
import com.example.safetrip.R.id.textLogin


class LoginSignup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
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
            val intent = Intent(this, LogInMain::class.java)
            startActivity(intent)
        }

    }
}