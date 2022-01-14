package com.example.safetrip

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat

class SettingsInfo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        val txtNameEdit = findViewById<TextView>(R.id.txtEditInfo)
        txtNameEdit.setOnClickListener {
            val intent = Intent(this, EditInfo::class.java)
            startActivity(intent)
        }

        val txtPinEdit = findViewById<TextView>(R.id.txtEditPin)
        txtPinEdit.setOnClickListener {
            val intent = Intent(this, EditPin::class.java)
            startActivity(intent)
        }
    }
}