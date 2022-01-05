package com.example.safetrip

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.safetrip.R
import com.example.safetrip.SettingsInfo
import com.google.firebase.database.FirebaseDatabase
import com.example.safetrip.database_adapter.UserName

class EditInfo : AppCompatActivity() {
    lateinit var updateFName: EditText
    lateinit var updateLName: EditText
    lateinit var btnUpdate: Button

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_info)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        updateFName = findViewById(R.id.editFirstName)
        updateLName = findViewById(R.id.editLastName)

        btnUpdate = findViewById(R.id.updateInfo)
        btnUpdate.setOnClickListener {
            updateInfo()
            startActivity(Intent(this, SettingsInfo::class.java))
        }
    }

    private fun updateInfo(){
        val preferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        val phonenumber = preferences.getString("PHONE_NUMBER", "NULL").toString()
        val point = preferences.getInt("POINTS", 0)
        val credit = preferences.getInt("CREDIT", 0)
        val email = preferences.getString("EMAIL", "NULL").toString()
        val pin = preferences.getString("PIN", "NULL").toString()

        val firstname = updateFName.text.toString().trim()
        val lastname = updateLName.text.toString().trim()

        if(firstname.isEmpty())
        {
            Toast.makeText(this, "Please enter first name", Toast.LENGTH_SHORT).show()
        }
        if(lastname.isEmpty())
        {
            Toast.makeText(this, "Please enter last name", Toast.LENGTH_SHORT).show()
        }
        else
        {
            val sharedPreferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("FIRST_NAME", firstname)
            editor.putString("LAST_NAME", lastname)
            editor.apply()

            val ref = FirebaseDatabase.getInstance().getReference("Names")
            val nameKey = ref.push().key

            val updatedName = UserName(nameKey, firstname, lastname, pin, phonenumber, email, credit, point)
            ref.child(phonenumber).setValue(updatedName).addOnSuccessListener {
                Toast.makeText(this, "Update Success", Toast.LENGTH_SHORT).show()
            }
        }
    }
}