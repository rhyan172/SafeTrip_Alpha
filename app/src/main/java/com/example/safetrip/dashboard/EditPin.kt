package com.example.safetrip

import android.content.Context
import android.content.Intent
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
    lateinit var oldPin: PinView
    lateinit var database: DatabaseReference
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_pin)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        var dataPin = ""

        val preferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        val currentPinNumber = preferences.getString("PHONE_NUMBER", "NULL").toString()
        database = FirebaseDatabase.getInstance().getReference("Names")
        database.child(currentPinNumber).get().addOnSuccessListener {
            if(it.exists())
            {
                val pin = it.child("pin").value
                val currentpin = pin.toString()
                dataPin = currentpin
            }
        }

        oldPin = findViewById(R.id.editPin)
        val oldpin = oldPin.text.toString()

        val buttonPinCurrent = findViewById<Button>(R.id.currentPinBtn)
        buttonPinCurrent.setOnClickListener {
            if(dataPin == oldpin){
                val intent = Intent(this, EditNewPin::class.java)
                startActivity(intent)
            }
            else
            {
                Toast.makeText(this, "pin empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
}