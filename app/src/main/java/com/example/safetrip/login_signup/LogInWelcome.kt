package com.example.safetrip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.safetrip.R.layout.activity_log_in_welcome

class LogInWelcome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_log_in_welcome)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)
    }
}