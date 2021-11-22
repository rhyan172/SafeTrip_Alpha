package com.example.safetrip

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.chaos.view.PinView
import com.example.safetrip.R.layout.activity_log_in_welcome
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_log_in_main.*
import kotlinx.android.synthetic.main.activity_log_in_welcome.*
import java.util.concurrent.TimeUnit

class LogInWelcome : AppCompatActivity() {
    lateinit var loginPin: PinView
    lateinit var preferences: SharedPreferences

    private lateinit var auth: FirebaseAuth

    private var storedVerificationId: String = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_log_in_welcome)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        auth = Firebase.auth

        preferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)

        val enteredPhoneNumber = preferences.getString("LOGIN_PIN", "NULL").toString()

        val pincode = findViewById<PinView>(R.id.pin_login_view)

        //pincode.setOnEditorActionListener( )
        if(pincode.itemCount == 4 && enteredPhoneNumber == pincode.toString())
        {
            Toast.makeText(this, "pin correct", Toast.LENGTH_SHORT).show()
        }
        else
        {
            Toast.makeText(this, "incorrect pin", Toast.LENGTH_SHORT).show()
        }

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks()
        {

            override fun onVerificationCompleted(credential: PhoneAuthCredential)
            {
                Log.d(TAG, "onVerificationCompleted:$credential")
                startActivity(Intent(applicationContext, LogInOtp::class.java))
                finish()
            }

            override fun onVerificationFailed(e: FirebaseException)
            {
                Log.w(TAG, "onVerificationFailed", e)
                Toast.makeText(applicationContext, "Verification Failed", Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d(TAG, "onCodeSent$verificationId")

                storedVerificationId = verificationId
                resendToken = token
            }
        }

        var currentUser = auth.currentUser
        if(currentUser != null) {
            startActivity(Intent(applicationContext, LogInWelcome::class.java))
            finish()
        }
    }

    private fun sendVerificationCode(number: String)
    {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
    }

    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String)
    {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
    }


    private fun signInWithPhone(credential: PhoneAuthCredential)
    {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if(task.isSuccessful)
                {
                    Log.d(TAG, "signInWithCredential: success")
                    Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show()
                    val user = task.result?.user
                }
                else
                {
                    Log.w(TAG, "signInWithCredential: failure", task.exception)
                    if(task.exception is FirebaseAuthInvalidCredentialsException)
                    {
                        Toast.makeText(this, "Verification code is invalid", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

}
