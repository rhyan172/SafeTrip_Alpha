package com.example.safetrip

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import androidx.core.content.ContextCompat
import com.chaos.view.PinView
import com.example.safetrip.R
import com.example.safetrip.R.layout.activity_log_in_welcome
import com.example.safetrip.DashboardMain
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_log_in_welcome.*
import java.util.concurrent.Executor

class LogInWelcome : AppCompatActivity() {

    private lateinit var preferences: SharedPreferences
    private lateinit var database: DatabaseReference

    private lateinit var btnFingerprint: ImageView

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: androidx.biometric.BiometricPrompt
    private lateinit var promptInfo: androidx.biometric.BiometricPrompt.PromptInfo
    private var userPin: String = ""
    private var FirstName: String = ""
    private var LastName: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_log_in_welcome)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        getUserPin()

        btnFingerprint = findViewById(R.id.finger_print)
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = androidx.biometric.BiometricPrompt(this, executor, object: androidx.biometric.BiometricPrompt.AuthenticationCallback(){
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(applicationContext, "error $errString", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationSucceeded(result: androidx.biometric.BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                startActivity(Intent(applicationContext, DashboardMain::class.java))
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(applicationContext, "Authentication Failed", Toast.LENGTH_SHORT).show()
            }
        })

        promptInfo=androidx.biometric.BiometricPrompt.PromptInfo.Builder()
            .setTitle("LOGIN")
            .setSubtitle("use fingerprint to login")
            .setNegativeButtonText("Cancel")
            .build()

        btnFingerprint.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }



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
                    if(pin == userPin){
                        startActivity(Intent(applicationContext, DashboardMain::class.java))
                        finish()
                    }
                    else
                    {
                        Toast.makeText(applicationContext, "Incorrect Pin", Toast.LENGTH_SHORT).show()
                        pincode.text?.clear()
                    }
                }
            }
        })

        val imgFinger = findViewById<ImageView>(R.id.finger_print)
        val txtTouch = findViewById<TextView>(R.id.textViewTouch)

        val fingerCheck = preferences.getBoolean("SWITCH_KEY", false)

        if(fingerCheck == true){
            imgFinger.visibility = View.VISIBLE
            txtTouch.visibility = View.VISIBLE
            biometricPrompt.authenticate(promptInfo)
        }
        else{
            imgFinger.visibility = View.GONE
            txtTouch.visibility = View.GONE
        }
    }

    private fun getUserPin(){
        preferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        val pln = preferences.getString("PHONE_NUMBER", "NULL").toString()
        database = FirebaseDatabase.getInstance().reference
        database.child("Names/$pln").get().addOnSuccessListener {
            if(it.exists()){
                val fname = it.child("first").value
                val lname = it.child("last").value
                val pin = it.child("pin").value

                userPin = pin.toString()
                FirstName = fname.toString()
                LastName = lname.toString()

                var fullName = "$FirstName $LastName"
                textViewN.text = fullName
            }
        }
    }
}
