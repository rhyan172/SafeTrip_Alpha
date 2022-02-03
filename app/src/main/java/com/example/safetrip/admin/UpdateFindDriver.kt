package com.example.safetrip

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_update_find_driver.*

class UpdateFindDriver : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_find_driver)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        findDriverBtn.setOnClickListener{
            val driverNum = findDriver.text.toString()
            database = FirebaseDatabase.getInstance().reference
            database.child("Driver/+63$driverNum").get().addOnSuccessListener {
                if(it.exists())
                {
                    sharedPreferences = getSharedPreferences("DRIVER_UPDATE", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("DRIVER_UPDATE_DRIVER", "+63$driverNum")
                    startActivity(Intent(this, UpdateDriver::class.java))
                }
                else
                {
                    Toast.makeText(this, "Driver does not exist.", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}