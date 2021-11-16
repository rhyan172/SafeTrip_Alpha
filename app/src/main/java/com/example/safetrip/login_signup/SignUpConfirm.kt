package com.example.safetrip

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.chaos.view.PinView

class SignUpConfirm : AppCompatActivity() {

    lateinit var preferences: SharedPreferences
    lateinit var sharedPreferences: SharedPreferences
    lateinit var confirmPC: PinView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_confirm)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        preferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        val passcode = preferences.getString("PIN_CODE", "NULL").toString()

        confirmPC= findViewById<PinView>(R.id.confirm_view)


        val btnNxt = findViewById<Button>(R.id.pinConfirm)
            btnNxt.setOnClickListener{
                val confirmVal = confirmPC.text.toString()
                if(confirmVal == passcode){
                    sharedPreferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
                    val passcode = confirmPC.text.toString()
                    val cpc = sharedPreferences.edit()
                    cpc.putString("CONFIRM_PIN_CODE", confirmVal)
                    cpc.apply()

                    val intent = Intent(this, SignUpName::class.java)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this, "incorrect passcode", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
