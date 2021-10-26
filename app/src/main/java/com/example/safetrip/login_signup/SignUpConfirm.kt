package com.example.safetrip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat

class SignUpConfirm : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_confirm)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)


        }
    }
