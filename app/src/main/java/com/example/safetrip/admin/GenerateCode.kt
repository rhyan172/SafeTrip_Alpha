package com.example.safetrip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat

class GenerateCode : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generate_code)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

    }
}