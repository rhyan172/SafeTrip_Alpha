package com.example.safetrip

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast

import androidx.core.content.ContextCompat
import com.chaos.view.PinView
import com.example.safetrip.R.layout.activity_log_in_welcome


class LogInWelcome : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_log_in_welcome)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        val preferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)

        val pincode = findViewById<PinView>(R.id.pin_login_view)

        pincode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                val pinval = pincode.text.toString()
                if(pinval.length == 4){
                    val pin = pincode.text.toString().trim()
                    val enteredPhoneNumber = preferences.getString("LOGIN_PIN", "NULL").toString()
                    if(pin == enteredPhoneNumber){
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                        finish()
                    }
                    else
                    {
                        Toast.makeText(applicationContext, "Incorrect Pin", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}
