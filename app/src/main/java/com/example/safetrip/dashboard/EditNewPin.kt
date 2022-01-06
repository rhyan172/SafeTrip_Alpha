package com.example.safetrip

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.chaos.view.PinView
import com.example.safetrip.R
import com.example.safetrip.database_adapter.UserName
import com.google.firebase.database.FirebaseDatabase

class EditNewPin : AppCompatActivity() {

    lateinit var newpin: PinView

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_new_pin)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        newpin = findViewById(R.id.editNewPin)
        val btnNewPin = findViewById<Button>(R.id.newPinBtn)
        btnNewPin.setOnClickListener {
            updatePin()
        }
    }

    private fun updatePin(){
        val preferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        val phonenumber = preferences.getString("PHONE_NUMBER", "NULL").toString()

        newpin = findViewById(R.id.editNewPin)
        val newPin = newpin.text.toString()

        if(newPin.isEmpty())
        {
            Toast.makeText(this, "Please enter your old pin", Toast.LENGTH_SHORT).show()
        }
        else
        {
            val ref = FirebaseDatabase.getInstance().getReference("Names")
            ref.child("Names/$phonenumber/pass").setValue(newPin).addOnSuccessListener {
                Toast.makeText(this, "Update Success", Toast.LENGTH_SHORT).show()
            }
        }
    }
}