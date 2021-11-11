package com.example.safetrip

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.safetrip.R
import com.example.safetrip.SignUpCode
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class SignUpNumber : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId:String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var phone: EditText
    //shared preference for capturing phone number to be use on database later on
    lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        auth= FirebaseAuth.getInstance()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_number)
        phone = findViewById<EditText>(R.id.phoneNumber)

        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                startActivity(Intent(applicationContext, SignUpName::class.java))
                finish()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(applicationContext, "Failed", Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d("TAG","onCodeSent:$verificationId")
                storedVerificationId = verificationId
                resendToken = token
                var intent = Intent(applicationContext,SignUpCode::class.java)
                intent.putExtra("storedVerificationId",storedVerificationId)
                startActivity(intent)
            }
        }

        val sign = findViewById<Button>(R.id.phoneNext)
        phone = findViewById(R.id.phoneNumber)

        var currentUser = auth.currentUser
        if(currentUser != null) {
            startActivity(Intent(applicationContext, SignUpName::class.java))
            finish()
        }

        sign.setOnClickListener{
            //shared preference for capturing phone number to be use on database later on
            sharedPreferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
            val pnumber = phone.text.toString()
            val edtr = sharedPreferences.edit()
            edtr.putString("PHONE_NUMBER", pnumber)
            edtr.apply()
            signUp()
        }
    }

    private fun signUp() {
        val mobileNumber=findViewById<EditText>(R.id.phoneNumber)
        var number=mobileNumber.text.toString().trim()

        if(!number.isEmpty()){
            number="+63"+number
            sendVerificationcode (number)
        }else{
            Toast.makeText(this,"Enter mobile number", Toast.LENGTH_SHORT).show()
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