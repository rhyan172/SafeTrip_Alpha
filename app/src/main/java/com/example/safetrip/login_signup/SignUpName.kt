package com.example.safetrip

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.safetrip.DashboardMain
import com.example.safetrip.database_adapter.UserName
import com.example.safetrip.LogInMain
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.dialog_view.view.*


class SignUpName : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var editFirstName: EditText
    lateinit var editLastName: EditText
    lateinit var editEmail: EditText
    lateinit var nameSave: Button
    lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_name)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        auth= FirebaseAuth.getInstance()
        var currentUser=auth.currentUser

        if(currentUser==null){
            startActivity(Intent(this, DashboardMain::class.java))
            finish()
        }

        editEmail = findViewById(R.id.eMail)
        editFirstName = findViewById(R.id.firstName)
        editLastName = findViewById(R.id.lastName)
        nameSave = findViewById(R.id.nameNext)

        nameSave.setOnClickListener()
        {
            saveName()
            val alertViewDialog = View.inflate(this, R.layout.dialog_view, null)

            val builder = AlertDialog.Builder(this)
            builder.setView(alertViewDialog)


            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.setCancelable(false)

            alertViewDialog.ok_Btn.setOnClickListener()
            {
                saveName()
                val intent = Intent(this, LogInMain::class.java)
                startActivity(intent)
                dialog.dismiss()
            }
        }

    }

    private fun saveName() {
        val firstName = editFirstName.text.toString().trim()
        val lastName = editLastName.text.toString().trim()
        val email = editEmail.text.toString().trim()
        val credit = 0
        val point = 0

        preferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        val numberPhone = preferences.getString("PHONE_NUMBER", "NULL").toString()
        val pincode = preferences.getString("CONFIRM_PIN_CODE", "NULL").toString()

        if(email.isEmpty())
        {
            Toast.makeText(this, "Please enter E-Mail", Toast.LENGTH_SHORT).show()
        }
        if (firstName.isEmpty())
        {
            Toast.makeText(this, "Please enter First Name", Toast.LENGTH_SHORT).show()
        }
        if (lastName.isEmpty())
        {
            Toast.makeText(this, "Please enter Last Name", Toast.LENGTH_SHORT).show()
        }
        else{
            val ref = FirebaseDatabase.getInstance().getReference("Names")
            val nameKey = ref.push().key

            val names = UserName(nameKey, firstName, lastName, pincode, "+63$numberPhone", email, credit, point)

            ref.child("+63$numberPhone").setValue(names).addOnCompleteListener {
                //Toast.makeText(applicationContext,"Name Saved Successfully", Toast.LENGTH_LONG).show()
            }
        }

    }
}