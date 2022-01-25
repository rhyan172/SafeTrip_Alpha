package com.example.safetrip

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.TimeUnit

class SignUpNumber : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var phone: EditText
    lateinit var sharedPreferences: SharedPreferences
    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_number)
        phone = findViewById(R.id.phoneNumber)

        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        val sign = findViewById<Button>(R.id.phoneNext)
        sign.setOnClickListener {
            signUp()
        }

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                startActivity(Intent(applicationContext, SignUpCode::class.java))
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

                Log.d("TAG", "onCodeSent:$verificationId")
                storedVerificationId = verificationId
                resendToken = token
                val intent = Intent(applicationContext, SignUpCode::class.java)
                intent.putExtra("storedVerificationId", storedVerificationId)
                startActivity(intent)
            }
        }

    }

    private fun signUp() {
        val mobileNumber = findViewById<EditText>(R.id.phoneNumber)
        var number = mobileNumber.text.toString()
        database = FirebaseDatabase.getInstance().reference
        database.child("Names/+63$number").get().addOnSuccessListener{
            if(it.exists()){
                if(!number.isEmpty()){
                    sharedPreferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    val pnumber = it.child("pnum").value
                    val numberP = pnumber.toString()
                    editor.putString("PHONE_NUMBER", numberP)
                    editor.apply()
                }
            }
            else{
                if (!number.isEmpty()) {
                    number = "+63" + number
                    sharedPreferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("PHONE_NUMBER", number)
                    editor.apply()
                    sendVerificationcode(number)
                } else {
                    Toast.makeText(this, "Enter mobile number", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun sendVerificationcode(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}