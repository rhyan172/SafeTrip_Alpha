package com.example.safetrip.dashboard

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.safetrip.DashboardMain
import com.example.safetrip.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_ride_complete.*
import kotlinx.android.synthetic.main.activity_safe_trip_location.*

class RideComplete : AppCompatActivity() {

    private lateinit var preferences: SharedPreferences
    private lateinit var database: DatabaseReference
    private var currentCreditUser: Float = 0.00F
    private var currentUserPoints: Int = 0
    private var currentDriverProfit: Float = 0.0F
    private var currentDriverEarnedPoints: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ride_complete)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        preferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE)
        val pnc = preferences.getString("PHONE_NUMBER", "NULL")
        val driverInfo = preferences.getString("DRIVER_INFORMATION", "NULL").toString()
        val payTotalFare = preferences.getFloat("FARE_TOTAL", 0.00F)
        val payPoints = preferences.getInt("POINTS_DEDUCT", 0)
        val pointSTL = preferences.getInt("POINTS_EARNED", 0)
        preferences = getSharedPreferences("SWITCH_FARE_POINTS", Context.MODE_PRIVATE)
        val sfp = preferences.getBoolean("SFP", false)


        if(sfp){

            textViewReceipt.text = payTotalFare.toString()
        }
        else if(!sfp)
        {
            textViewPeso.setText("PTS.")
            textViewReceipt.text = payPoints.toString()
        }

        database = FirebaseDatabase.getInstance().reference
        database.child("Names/$pnc").get().addOnSuccessListener {
            if(it.exists()){
                val currentBal = it.child("credits").value
                currentCreditUser = currentBal.toString().toFloat()

                val currentPoints = it.child("points").value
                currentUserPoints = currentPoints.toString().toInt()
            }
        }
        database.child("Driver/$driverInfo").get().addOnSuccessListener {
            if(it.exists()){
                val driversProfit = it.child("driverProfit").value
                val driversPointEarned = it.child("driverPointsEarned").value

                currentDriverProfit = driversProfit.toString().toFloat()
                currentDriverEarnedPoints = driversPointEarned.toString().toInt()
            }
        }

        btnRideReceipt.setOnClickListener{
            if(sfp == true) {
                val updateCredit = currentCreditUser - payTotalFare
                val updateAddUserPoints =  pointSTL + currentUserPoints
                val updateCreditS = updateCredit.toString()
                val database = FirebaseDatabase.getInstance().reference
                database.child("Names/$pnc/credits").setValue(updateCreditS)
                database.child("Names/$pnc/points").setValue(updateAddUserPoints)

                val updateDriverProfit = currentDriverProfit + payTotalFare
                val updateDriverProfitToString = updateDriverProfit.toString()
                database.child("Driver/$driverInfo/driverProfit").setValue(updateDriverProfitToString)
                startActivity(Intent(this, DashboardMain::class.java))
                finish()
            }
            else if(sfp == false){
                val updatePoints = currentUserPoints - payPoints
                val database = FirebaseDatabase.getInstance().reference
                database.child("Names/$pnc/points").setValue(updatePoints)

                val updateDriverEarnedPoints = currentDriverEarnedPoints + payPoints
                database.child("Driver/$driverInfo/driverPointsEarned").setValue(updateDriverEarnedPoints)
                startActivity(Intent(this, DashboardMain::class.java))
                finish()
            }
        }
    }
}