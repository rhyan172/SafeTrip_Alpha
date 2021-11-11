package com.example.safetrip

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.safetrip.R.id.phoneVerify
import com.example.safetrip.R.layout.activity_sign_up_code
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class SignUpCode : AppCompatActivity() {
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_code)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        auth= FirebaseAuth.getInstance()

        val intentValue = intent.getStringExtra("Data")
        findViewById<TextView>(R.id.secondTextView).apply{
            text = intentValue.toString()
        }

        val button = findViewById<Button>(R.id.phoneVerify)
        button.setOnClickListener {
            val intent = Intent(this, SignUpName::class.java)

            startActivity(intent)
        }

        val storedVerificationId=intent.getStringExtra("storedVerificationId")

        val verify = findViewById<Button>(R.id.phoneVerify)
        val otpGiven = findViewById<EditText>(R.id.otp_view)

        verify.setOnClickListener{
            var otp=otpGiven.text.toString().trim()
            if(!otp.isEmpty()){
                val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(
                    storedVerificationId.toString(), otp)
                signInWithPhoneAuthCredential(credential)
            }else{
                Toast.makeText(this,"Enter OTP", Toast.LENGTH_SHORT).show()
            }

        }

    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(applicationContext, SignUpName::class.java))
                    finish()
// ...
                } else {
// Sign in failed, display a message and update the UI
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
// The verification code entered was invalid
                        Toast.makeText(this,"Invalid OTP", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}