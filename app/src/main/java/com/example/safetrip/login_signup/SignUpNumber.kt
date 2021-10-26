package com.example.safetrip

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat

class SignUpNumber : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_number)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        val button = findViewById<Button>(R.id.phoneNext)
        button.setOnClickListener {
            val intent = Intent(this, SignUpCode::class.java)
            startActivity(intent)
        }
    }
}