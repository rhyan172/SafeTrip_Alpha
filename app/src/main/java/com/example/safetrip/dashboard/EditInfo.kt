package com.example.safetrip

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_edit_info.*

class EditInfo : AppCompatActivity() {
    private var updateFName: String = ""
    private var updateLName: String = ""
    private var updateEmail: String = ""

    private lateinit var btnUpdate: Button
    private lateinit var preferences: SharedPreferences

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_info)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        fetchData()

        btnUpdate = findViewById(R.id.updateInfo)
        btnUpdate.setOnClickListener {
            updateInfo()
            finish()
        }
    }

    private fun updateInfo(){
        preferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        val phonenumber = preferences.getString("PHONE_NUMBER", "NULL").toString()

        val updatedFName = editFirstName.text.toString()
        val updatedLName = editLastName.text.toString()
        val updatedEmail = editEmail.text.toString()

        val database = FirebaseDatabase.getInstance().reference
        database.child("Names/$phonenumber/first").setValue(updatedFName)
        database.child("Names/$phonenumber/last").setValue(updatedLName)
        database.child("Names/$phonenumber/email").setValue(updatedEmail)
        Toast.makeText(this, "updating information success", Toast.LENGTH_SHORT).show()
    }

    private fun fetchData(){
        preferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        val phonenumber = preferences.getString("PHONE_NUMBER", "NULL").toString()
        val database = FirebaseDatabase.getInstance().reference
        database.child("Names/$phonenumber").get().addOnSuccessListener {
            if(it.exists())
            {
                val fName = it.child("first").value
                val lName = it.child("last").value
                val eMail = it.child("email").value

                updateFName = fName.toString()
                updateLName = lName.toString()
                updateEmail = eMail.toString()

                editFirstName.setText(updateFName)
                editLastName.setText(updateLName)
                editEmail.setText(updateEmail)
            }
        }
    }
}