package com.example.safetrip

import android.content.Intent
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

    lateinit var ref : DatabaseReference
    lateinit var LoginPhone: EditText

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
        ref.child(phoneLogin).get().addOnSuccessListener {

            if(it.exists()){
                val pnumber = it.child("pnum").value
                val numberP = LoginPhone.text.toString()
                val pn = "+63$numberP"
                    if(pn == pnumber){
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