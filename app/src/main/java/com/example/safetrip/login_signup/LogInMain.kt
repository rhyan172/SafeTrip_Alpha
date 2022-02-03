package com.example.safetrip

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.safetrip.R.id.logInBtn
import com.example.safetrip.R.layout.activity_log_in_main
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LogInMain : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var loginPhone: EditText
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_log_in_main)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        loginPhone = findViewById(R.id.logInPhone)
        sharedPreferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)

        val btnLogin = findViewById<Button>(logInBtn)
        btnLogin.setOnClickListener {

            val checkPhoneNum = loginPhone.text.toString()
            database = FirebaseDatabase.getInstance().reference
            sharedPreferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
            database.child("Names/+63$checkPhoneNum").get().addOnSuccessListener {
                if(it.exists())
                {
                    val pnumber = it.child("pnum").value
                    val numberP = pnumber.toString()
                    val editor = sharedPreferences.edit()
                    editor.putString("PHONE_NUMBER", numberP)
                    editor.apply()
                    login()

                }
                else
                {
                    //Toast.makeText(this, "User doesn't Exist", Toast.LENGTH_SHORT).show()
                    loginDriver()
                    loginAdmin()
                }
            }
        }
    }

    private fun login() {
        val intent = Intent(this, LogInWelcome::class.java)
        startActivity(intent)
    }

    private fun loginDriver(){
        database = FirebaseDatabase.getInstance().reference
        sharedPreferences = getSharedPreferences("DRIVER", Context.MODE_PRIVATE)
        val checkPhoneNum = loginPhone.text.toString()
        database.child("Driver/+63$checkPhoneNum").get().addOnSuccessListener {
            if(it.exists())
            {
                val dnumber = it.child("driverNumber").value
                val dNumber = dnumber.toString()
                val editor = sharedPreferences.edit()
                editor.putString("DRIVER_NUMBER", dNumber)
                editor.apply()
                startActivity(Intent(this, DriverDetails::class.java))
            }
            else
            {
                //Toast.makeText(this, "driver does not exist", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginAdmin(){
        database = FirebaseDatabase.getInstance().reference
        sharedPreferences = getSharedPreferences("ADMIN", Context.MODE_PRIVATE)
        val checkPhoneNumAdmin = loginPhone.text.toString()
        database.child("Admin/+63$checkPhoneNumAdmin").get().addOnSuccessListener {
            if(it.exists())
            {
                val adminNumber = it.child("adminNumber").value
                val aNumber = adminNumber.toString()
                val editor = sharedPreferences.edit()
                editor.putString("ADMIN_NUMBER", aNumber)
                editor.apply()
                startActivity(Intent(this, Admin::class.java))
            }
            else
            {
                //Toast.makeText(this, "admin does not exist", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

