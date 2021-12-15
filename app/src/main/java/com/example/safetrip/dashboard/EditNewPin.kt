package com.example.safetrip

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.chaos.view.PinView
import com.example.safetrip.R
import com.example.safetrip.database_adapter.UserName
import com.google.firebase.database.FirebaseDatabase

class EditNewPin : AppCompatActivity() {

    lateinit var newpin: PinView

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
        val point = preferences.getInt("POINTS", 0)
        val credit = preferences.getInt("CREDIT", 0)
        val email = preferences.getString("EMAIL", "NULL").toString()
        val firstnamePin = preferences.getString("FIRST_NAME", "NULL").toString()
        val lastnamePin = preferences.getString("LAST_NAME", "NULL").toString()

        newpin = findViewById(R.id.editNewPin)
        val newPin = newpin.text.toString()

        if(newPin.isEmpty())
        {
            Toast.makeText(this, "Please enter your old pin", Toast.LENGTH_SHORT).show()
        }
        else
        {
            val ref = FirebaseDatabase.getInstance().getReference("Names")
            val nameKey = ref.push().key

            val updatedPin = UserName(nameKey, firstnamePin, lastnamePin, newPin, phonenumber, email, credit, point)
            ref.child(phonenumber).setValue(updatedPin).addOnSuccessListener {
                Toast.makeText(this, "Update Success", Toast.LENGTH_SHORT).show()
            }
        }
    }
}