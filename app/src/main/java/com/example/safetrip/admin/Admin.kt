package com.example.safetrip

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_admin.*

class Admin : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        database = FirebaseDatabase.getInstance().reference
        preferences = getSharedPreferences("ADMIN", Context.MODE_PRIVATE)
        val adminNum = preferences.getString("ADMIN_NUM", "admin-num")

        database.addValueEventListener(object: ValueEventListener{
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                database.child("Admin/$adminNum").get().addOnSuccessListener{
                    if(it.exists())
                    {
                        val adminName = it.child("name").value
                        textViewName.text = adminName.toString()
                    }
                    else
                    {
                        textViewName.text = "Administrator"
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


        //update fare button
        textViewUpdateFare.setOnClickListener{
            startActivity(Intent(this, UpdateFare::class.java))
        }
        imageView5.setOnClickListener{
            startActivity(Intent(this, UpdateFare::class.java))
        }

        //add driver button
        textViewAddDriver.setOnClickListener{
            startActivity(Intent(this, AddDriver::class.java))
        }
        imageView6.setOnClickListener{
            startActivity(Intent(this, AddDriver::class.java))
        }

        //deactivate driver button
        textViewDeactivateDriver.setOnClickListener{
            startActivity(Intent(this, DeactivateDriver::class.java))
        }
        imageView8.setOnClickListener{
            startActivity(Intent(this, DeactivateDriver::class.java))
        }

        //qr code button
        textViewGenerateQr.setOnClickListener{
            startActivity(Intent(this, GenerateCode::class.java))
        }
        imageView9.setOnClickListener{
            startActivity(Intent(this, GenerateCode::class.java))
        }

        //logout button
        adminLogoutBtn.setOnClickListener{
            startActivity(Intent(this, LoginSignup::class.java))
            this.finish()
        }
    }
}