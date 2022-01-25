package com.example.safetrip.login_signup

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.chaos.view.PinView
import com.example.safetrip.LogInWelcome
import com.example.safetrip.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_reset_pin.*

class ResetPin : AppCompatActivity() {

    private lateinit var preferences: SharedPreferences
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_pin)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        database = FirebaseDatabase.getInstance().reference
        preferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)

        btnNewPin.setOnClickListener{
            val newpin = findViewById<PinView>(R.id.editNewPin).text.toString().trim()
            val pnc = preferences.getString("PHONE_NUMBER", "phone-number").toString()
            database.child("Names/$pnc/pin").setValue(newpin)
            Toast.makeText(this, "Pincode change Success!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LogInWelcome::class.java))
        }
    }
}