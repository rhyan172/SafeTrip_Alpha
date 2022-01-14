package com.example.safetrip

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat

import android.widget.Switch
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.safetrip.R
import java.util.concurrent.Executor

class Fingerprint : AppCompatActivity() {

    lateinit var fingerSwitch: Switch

    lateinit var executor: Executor
    lateinit var biometricPrompt: androidx.biometric.BiometricPrompt
    lateinit var promptInfo: androidx.biometric.BiometricPrompt.PromptInfo

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fingerprint)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = androidx.biometric.BiometricPrompt(this, executor, object: androidx.biometric.BiometricPrompt.AuthenticationCallback(){
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(applicationContext, "error $errString", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationSucceeded(result: androidx.biometric.BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                val sharedPreferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putBoolean("SWITCH_KEY", true)
                editor.apply()
                Toast.makeText(applicationContext, "Fingerprint Enabled", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(applicationContext, "Authentication Failed", Toast.LENGTH_SHORT).show()
            }
        })

        promptInfo=androidx.biometric.BiometricPrompt.PromptInfo.Builder()
            .setTitle("Enable Fingerprint")
            .setSubtitle("Login using fingerprint")
            .setNegativeButtonText("Cancel")
            .build()


        val sharedPreferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        fingerSwitch = findViewById(R.id.finger_print_Switch)
        fingerSwitch.isChecked = sharedPreferences.getBoolean("SWITCH_KEY", false)
        fingerSwitch.setOnCheckedChangeListener{ _, isChecked ->
            if(isChecked){
                biometricPrompt.authenticate(promptInfo)
            }
            else{
                editor.putBoolean("SWITCH_KEY", false)
                editor.apply()
                Toast.makeText(applicationContext, "Fingerprint Disabled", Toast.LENGTH_SHORT).show()
            }
        }
    }
}