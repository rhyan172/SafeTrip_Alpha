package com.example.safetrip

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.safetrip.R
import com.example.safetrip.dashboard.SafeTripLocation
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import kotlinx.android.synthetic.main.activity_scan_pay.*

class ScanPay : AppCompatActivity() {

    private lateinit var fare: TextView
    private lateinit var database: DatabaseReference
    private var totalAmount: Float = 0.00F
    private lateinit var preferences: SharedPreferences
    private var scanPayCredit: Float = 0.00F
    private var earnedP: Int = 0
    private var pointsFromDB: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_pay)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        fare = findViewById(R.id.textViewTotal)

        var increment = 1
        var farePay = 0.00F

        earnedP = numPassenger.getText().toString().toInt()


        preferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        val phoneNumScanPay = preferences.getString("PHONE_NUMBER", "NULL")
        database = FirebaseDatabase.getInstance().reference
        database.child("Fare").get().addOnSuccessListener {
            if(it.exists())
            {
                val price = it.child("price").value
                farePay = price.toString().toFloat()
                fare.text = price.toString()

            }
        }
        database.child("Names/$phoneNumScanPay").get().addOnSuccessListener {
            if(it.exists())
            {
                val scanPayCred = it.child("credits").value
                scanPayCredit = scanPayCred.toString().toFloat()
            }
        }
        database.child("Points").get().addOnSuccessListener {
            if(it.exists())
            {
                val pointsAdd = it.child("ePoints").value
                pointsFromDB = pointsAdd.toString().toInt()
            }
        }

        fare.text = farePay.toString()
        scanPay.setOnClickListener() {
            val sAlertDialogBuilder = AlertDialog.Builder(this)
            sAlertDialogBuilder.setTitle("Payment Confirmation")
            sAlertDialogBuilder.setMessage("Please Confirm your Payment")
            sAlertDialogBuilder.setCancelable(false)
            sAlertDialogBuilder.setPositiveButton("YES") { dialog, id ->
                if(totalAmount > scanPayCredit){
                    Toast.makeText(this, "Insufficient Balance. Please cash in immediately.", Toast.LENGTH_SHORT).show()
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

        plusBtn.setOnClickListener(){
            increment++
            totalAmount = (farePay * increment)
            numPassenger.setText(increment.toString())
            fare.text = totalAmount.toString()
        }
        minusBtn.setOnClickListener(){
            increment--
            totalAmount -= farePay
            numPassenger.setText(increment.toString())
            fare.text = totalAmount.toString()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result: IntentResult =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
            } else {
                passData()
                switchForFare()
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

    private fun passData(){
        val sharedPreferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val fareTotal = fare.text.toString().toFloat()
        val totalPasenger = numPassenger.getText().toString().toInt()
        val totalPointsEarned = totalPasenger * pointsFromDB
        editor.putFloat("FARE_TOTAL", fareTotal)
        editor.putInt("POINTS_EARNED", totalPointsEarned)
        editor.apply()
    }

    private fun switchForFare(){
        val sharedPreferences = getSharedPreferences("SWITCH_FARE_POINTS", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("SFP", true)
        editor.apply()
    }
}
