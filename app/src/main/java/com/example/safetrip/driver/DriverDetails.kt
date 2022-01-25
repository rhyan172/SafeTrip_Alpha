package com.example.safetrip

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_driver_details.*

class DriverDetails : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_details)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        preferences = getSharedPreferences("DRIVER", Context.MODE_PRIVATE)
        val dn = preferences.getString("DRIVER_NUMBER", "driver-number").toString()

        database = FirebaseDatabase.getInstance().reference

        database.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                database.child("Driver/$dn").get().addOnSuccessListener {
                    val drivername = it.child("driverName").value
                    val drivernum = it.child("driverNumber").value
                    val driverpe = it.child("driverPointsEarned").value
                    val driverprofit = it.child("driverProfit").value

                    txtNameDriver.text = drivername.toString()
                    txtNumDriver.text = drivernum.toString()
                    txtPointsDriver.text = driverpe.toString()
                    txtTotalDriver.text = driverprofit.toString()
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })

        logoutDriverBtn.setOnClickListener{
            startActivity(Intent(this, LoginSignup::class.java))
            this.finish()
        }
    }
}