package com.example.safetrip

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.biometrics.BiometricManager
import android.hardware.biometrics.BiometricPrompt
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import androidx.core.content.ContextCompat
import androidx.core.os.CancellationSignal
import androidx.core.view.isVisible
import com.chaos.view.PinView
import com.example.safetrip.R.layout.activity_log_in_welcome
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up_number.*
import org.w3c.dom.Text
import java.util.concurrent.Executor


class LogInWelcome : AppCompatActivity() {

    lateinit var preferences: SharedPreferences
    lateinit var reference: DatabaseReference

    lateinit var btnFingerprint: ImageView

    lateinit var executor: Executor
    lateinit var biometricPrompt: androidx.biometric.BiometricPrompt
    lateinit var promptInfo: androidx.biometric.BiometricPrompt.PromptInfo


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_log_in_welcome)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        preferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)

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
            .setTitle("Biometric Authentication")
            .setSubtitle("Login using fingerprint")
            .setNegativeButtonText("Cancel")
            .build()

        btnFingerprint.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }


        val pincode = findViewById<PinView>(R.id.pin_login_view)

        val FirstName = preferences.getString("FIRST_NAME", "NULL").toString()
        val LastName = preferences.getString("LAST_NAME", "NULL").toString()


        val fullName = findViewById<TextView>(R.id.textViewN)
        fullName.text = "$FirstName $LastName"
        pincode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                val pinval = pincode.text.toString()
                if(pinval.length == 4){
                    val pin = pincode.text.toString().trim()
                    val enteredPhoneNumber = preferences.getString("PIN", "NULL").toString()
                    if(pin == enteredPhoneNumber){
                        startActivity(Intent(applicationContext, DashboardMain::class.java))
                        finish()
                    }
                    else
                    {
                        Toast.makeText(applicationContext, "Incorrect Pin", Toast.LENGTH_SHORT).show()
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
        }
        else{
            imgFinger.visibility = View.GONE
            txtTouch.visibility = View.GONE
        }
    }
}
