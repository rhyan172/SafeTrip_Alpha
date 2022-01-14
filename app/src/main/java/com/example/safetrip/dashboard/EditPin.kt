package com.example.safetrip

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.chaos.view.PinView
import com.example.safetrip.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditPin : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private var dataPin: String = ""

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_pin)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        val preferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        val phoneNum = preferences.getString("PHONE_NUMBER", "NULL").toString()
        database = FirebaseDatabase.getInstance().reference
        database.child("Names/$phoneNum").get().addOnSuccessListener {
            if(it.exists())
            {
                val pin = it.child("pin").value
                val currentpin = pin.toString()
                dataPin = currentpin
            }
        }

        val buttonPinCurrent = findViewById<Button>(R.id.currentPinBtn)
        buttonPinCurrent.setOnClickListener {
            val oldPin = findViewById<PinView>(R.id.editPin)
            val oldpinUser = oldPin.text.toString()
            if(oldpinUser == dataPin){
                val intent = Intent(this, EditNewPin::class.java)
                startActivity(intent)
                finish()
            }
            else if(oldpinUser.isEmpty())
            {
                Toast.makeText(this, "pin empty", Toast.LENGTH_SHORT).show()
            }
            else if(oldpinUser != dataPin){
                Toast.makeText(this, "incorrect pin", Toast.LENGTH_SHORT).show()
            }
        }
    }
}