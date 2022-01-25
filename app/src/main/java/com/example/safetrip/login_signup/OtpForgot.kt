package com.example.safetrip.login_signup

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.chaos.view.PinView
import com.example.safetrip.R
import com.example.safetrip.SignUpPin
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_otp_forgot.*
import java.util.concurrent.TimeUnit

class OtpForgot : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var storedVerificationId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_forgot)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        textResendCodeOTP.setOnClickListener{
            val preferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
            val pnc = preferences.getString("PHONE_NUMBER", "PHONE-NUMBER").toString()
            resendOtpCode(pnc)
            Toast.makeText(this, "OTP sent", Toast.LENGTH_SHORT).show()
        }

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Toast.makeText(applicationContext, "OTP is sent to your number.", Toast.LENGTH_SHORT).show()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(applicationContext, "Failed", Toast.LENGTH_LONG).show()
                finish()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                storedVerificationId = verificationId
                resendToken = token
            }
        }

        storedVerificationId = intent.getStringExtra("storedVerificationId").toString()

        phoneVerify.setOnClickListener{
            val verifyOtp = findViewById<PinView>(R.id.otp_view).text.toString().trim()
            if(!verifyOtp.isEmpty())
            {
                val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(
                    storedVerificationId, verifyOtp)
                signInWithPhone(credential)

            }
        }
    }

    private fun signInWithPhone(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, ResetPin::class.java))
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

    private fun resendOtpCode(pnc: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(pnc) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}