package com.example.safetrip.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.safetrip.R

class CashIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cash_in)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)


    }
}