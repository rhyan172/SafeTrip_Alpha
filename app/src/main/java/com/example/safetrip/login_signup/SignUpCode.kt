package com.example.safetrip

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat
import com.example.safetrip.R.id.phoneVerify
import com.example.safetrip.R.layout.activity_sign_up_code

class SignUpCode : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_sign_up_code)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        val button = findViewById<Button>(phoneVerify)
        button.setOnClickListener {
            val intent = Intent(this, SignUpName::class.java)
            startActivity(intent)
        }

    }
}