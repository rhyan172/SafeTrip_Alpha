package com.example.safetrip

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.safetrip.login_signup.UserName
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class SignUpName : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var editFirstName: EditText
    lateinit var editLastName: EditText
    lateinit var nameSave: Button
    lateinit var phoneNum: EditText
    lateinit var numBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_name)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)



        auth= FirebaseAuth.getInstance()
        var currentUser=auth.currentUser

        if(currentUser==null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

        editFirstName = findViewById(R.id.firstName)
        editLastName = findViewById(R.id.lastName)
        nameSave = findViewById(R.id.nameNext)

        nameSave.setOnClickListener{
            saveName()
            val intent = Intent(this, SignUpPin::class.java)
            startActivity(intent)
        }


    }

    private fun saveName() {
        val sharedPreferences = getSharedPreferences("PHONE_NUMBER", Context.MODE_PRIVATE)
        val firstName = editFirstName.text.toString().trim()
        val lastName = editLastName.text.toString().trim()
        val numberPhone = sharedPreferences.getString("PNumber", "defaultNumber").toString()

        if (firstName.isEmpty())
        {
            editFirstName.error = "Please enter First Name"
            return
        }
        if (lastName.isEmpty())
        {
            editLastName.error = "Please enter Last Name"
            return
        }

        val ref = FirebaseDatabase.getInstance().getReference("Names")
        val nameKey = ref.push().key

        val names = UserName(nameKey, firstName, lastName, numberPhone)

        ref.child(nameKey.toString()).setValue(names).addOnCompleteListener {
            Toast.makeText(applicationContext,"Name Saved Successfully", Toast.LENGTH_LONG).show()
        }

    }
}