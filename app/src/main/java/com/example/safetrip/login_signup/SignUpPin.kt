package com.example.safetrip

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat
import com.chaos.view.PinView

class SignUpPin : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_pin)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        val pincode = findViewById<PinView>(R.id.pin_view)

        val button = findViewById<Button>(R.id.pinNext)
        button.setOnClickListener {
            //shared preference for pin to match in confirm pin
            sharedPreferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
            val passcode = pincode.text.toString()
            val pin = sharedPreferences.edit()
            pin.putString("PIN_CODE", passcode)
            pin.apply()

            val intent = Intent(this, SignUpConfirm::class.java)
            startActivity(intent)
        }
    }
}