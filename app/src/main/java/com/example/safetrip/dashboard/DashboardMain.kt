package com.example.safetrip

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.safetrip.dashboard.SafeTripLocation
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import kotlinx.android.synthetic.main.activity_dashboard_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_rewards.*

class DashboardMain : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var preferences: SharedPreferences
    private var totalUserPoints: Int = 0
    private var pointsDeduct: Int = 0

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_main)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        val firstFragment = HomeFragment()
        val secondFragment = PaymentFragment()
        val thirdFragment = RewardsFragment()
        val fourthFragment = SettingsFragment()

        setCurrentFragment(firstFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> setCurrentFragment(firstFragment)
                R.id.payment ->setCurrentFragment(secondFragment)
                R.id.reward ->setCurrentFragment(thirdFragment)
                R.id.settings ->setCurrentFragment(fourthFragment)
            }
            true
        }


    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment).commit()
        }

    internal fun redeemPoints(){
        val sAlertDialogBuilder = AlertDialog.Builder(this)
        sAlertDialogBuilder.setTitle("Redeem Rewards")
        sAlertDialogBuilder.setMessage("Are you sure you want to redeem your rewards?")
        sAlertDialogBuilder.setCancelable(false)
        sAlertDialogBuilder.setPositiveButton("YES") { dialog, id ->
            if(pointsDeduct > totalUserPoints){
                Toast.makeText(this, "Insufficient Points.", Toast.LENGTH_SHORT).show()
            }
            else{
                val scanner = IntentIntegrator(this)
                scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
                scanner.setBeepEnabled(false)
                scanner.initiateScan()
            }
        }
        sAlertDialogBuilder.setNegativeButton("NO") { dialog, id ->
            Toast.makeText(this, "Transaction Cancelled", Toast.LENGTH_SHORT).show()

        }
        val sAlertDialog = sAlertDialogBuilder.create()
        sAlertDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result: IntentResult =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
            } else {
                val sharedPreferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("DRIVER_INFORMATION", result.contents)
                editor.apply()
                Toast.makeText(this, "Scanned Success", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, SafeTripLocation::class.java))
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    internal fun getPointsData(){
        preferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        val rewardsPhoneNumber = preferences.getString("PHONE_NUMBER", "NULL")
        database = FirebaseDatabase.getInstance().reference
        database.child("Names/$rewardsPhoneNumber").get().addOnSuccessListener {
            if(it.exists()){
                val points = it.child("points").value
                totalUserPoints = points.toString().toInt()
            }
        }
        database.child("Fare").get().addOnSuccessListener {
            if(it.exists()){
                val points = it.child("reward").value
                pointsDeduct = points.toString().toInt()

                val sharedPreferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putInt("POINTS_DEDUCT", points.toString().toInt())
                editor.apply()
            }
        }
    }
}