package com.example.safetrip

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat

class EditPin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_pin)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        val buttonPinCurrent = findViewById<Button>(R.id.currentPinBtn)
        buttonPinCurrent.setOnClickListener {
            val intent = Intent(this, EditNewPin::class.java)
            startActivity(intent)
        }
    }
}