package com.example.safetrip

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat

class SignUpPin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_pin)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        val button = findViewById<Button>(R.id.pinNext)
        button.setOnClickListener {
            val intent = Intent(this, SignUpConfirm::class.java)
            startActivity(intent)
        }
    }
}