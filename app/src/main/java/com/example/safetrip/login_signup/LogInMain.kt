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
import com.example.safetrip.R
import com.example.safetrip.R.id.logInBtn
import com.example.safetrip.R.layout.activity_log_in_main
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class LogInMain : AppCompatActivity() {

    lateinit var ref : DatabaseReference
    lateinit var LoginPhone: EditText
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_log_in_main)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        ref = FirebaseDatabase.getInstance().getReference("Names")

        LoginPhone = findViewById<EditText>(R.id.logInPhone)

        val button = findViewById<Button>(logInBtn)
        button.setOnClickListener {
            val phoneLogin = LoginPhone.text.toString()
            if(phoneLogin.isNotEmpty()){
                login(phoneLogin)
            }
            else
            {
                Toast.makeText(this, "Phone number is empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun login(phoneLogin: String) {
        ref = FirebaseDatabase.getInstance().getReference("Names")
        ref.child("+63$phoneLogin").get().addOnSuccessListener {
            sharedPreferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
            val pnumber = it.child("pnum").value
            val numberP = pnumber.toString()
            val editor = sharedPreferences.edit()
            editor.putString("PHONE_NUMBER", numberP)
            //get user name
            val fName = it.child("first").value
            val lName = it.child("last").value
            //for settings
            val FName = fName.toString()
            val LName = lName.toString()
            editor.putString("FIRST_NAME", FName)
            editor.putString("LAST_NAME", LName)
            //for getting email and credits
            val dEmail = it.child("email").value
            //for update data
            val DEmail = dEmail.toString()
            editor.putString("EMAIL", DEmail)
            editor.apply()

            if(it.exists()){
                //get user pin for login use
                val pnumber = it.child("pnum").value
                val numberP = LoginPhone.text.toString()
                val pn = "+63$numberP"
                    if(pn == pnumber){
                        val pinCode = it.child("pin").value
                        val pc = pinCode.toString()
                        val editor = sharedPreferences.edit()
                        editor.putString("PIN", pc)
                        editor.apply()
                        val intent = Intent(this, LogInWelcome::class.java)
                        startActivity(intent)
                    }
            }
            else
            {
                Toast.makeText(this, "User doesn't exist", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
