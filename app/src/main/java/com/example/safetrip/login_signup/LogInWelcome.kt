package com.example.safetrip

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.chaos.view.PinView
import com.example.safetrip.R.layout.activity_log_in_welcome
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LogInWelcome : AppCompatActivity() {
    lateinit var ref : DatabaseReference
    lateinit var loginPin: PinView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_log_in_welcome)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        loginPin = findViewById<PinView>(R.id.pin_login_view)
        val pinLogin = loginPin.text.toString()

    }

}
