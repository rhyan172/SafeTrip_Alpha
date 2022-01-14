package com.example.safetrip

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.chaos.view.PinView
import com.google.firebase.database.FirebaseDatabase

class EditNewPin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_new_pin)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        val btnNewPin = findViewById<Button>(R.id.btnNewPin)
        btnNewPin.setOnClickListener {
            val preferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
            val phonenumber = preferences.getString("PHONE_NUMBER", "NULL").toString()

            val newpin = findViewById<PinView>(R.id.editNewPin)
            val newPin = newpin.text.toString()

            if(newPin.isNotEmpty())
            {
                val database = FirebaseDatabase.getInstance().reference
                database.child("Names/$phonenumber/pin").setValue(newPin)
                Toast.makeText(this, "Update Success", Toast.LENGTH_SHORT).show()
                finish()
            }
            else if(newPin.isEmpty())
            {
                Toast.makeText(this, "Please enter your new pin", Toast.LENGTH_LONG).show()
            }
        }
    }
}