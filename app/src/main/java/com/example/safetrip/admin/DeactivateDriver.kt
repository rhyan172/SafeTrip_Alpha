package com.example.safetrip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_deactivate_driver.*

class DeactivateDriver : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var deactivateDriver: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deactivate_driver)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        deactivateDriver = findViewById(R.id.deactivateDriver)

        database = FirebaseDatabase.getInstance().reference
        deactivateDriverBtn.setOnClickListener{

            val driverNumberDeactivation = deactivateDriver.text.toString()
            if(driverNumberDeactivation.isNotEmpty())
            {
                database.child("Driver/+63$driverNumberDeactivation").get().addOnSuccessListener {
                    if(it.exists())
                    {
                        val driverDeactivation = deactivateDriver.text.toString()
                        database.child("Driver/+63$driverDeactivation").removeValue().addOnSuccessListener {
                            Toast.makeText(this, "Driver Deactivated.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else
                    {
                        Toast.makeText(this, "Driver does not exist.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else
            {
                Toast.makeText(this, "Field is empty. Please input the Driver's number.", Toast.LENGTH_LONG).show()
            }
        }
    }
}