package com.example.safetrip

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat
import com.example.safetrip.R.id.logInBtn
import com.example.safetrip.R.layout.activity_log_in_main


class LogInMain : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_log_in_main)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        val button = findViewById<Button>(logInBtn)
        button.setOnClickListener {
            val intent = Intent(this, LogInWelcome::class.java)
            startActivity(intent)
        }
    }
}