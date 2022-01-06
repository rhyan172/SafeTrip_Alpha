package com.example.safetrip

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.safetrip.R
import com.example.safetrip.dashboard.SafeTripLocation
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import kotlinx.android.synthetic.main.activity_scan_pay.*

class ScanPay : AppCompatActivity() {

    private lateinit var fare: TextView
    private var totalAmount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_pay)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        fare = findViewById(R.id.textViewTotal)

        var increment = 1
        val farePay: Int = 15
        fare.text = farePay.toString()
        scanPay.setOnClickListener() {
            val sAlertDialogBuilder = AlertDialog.Builder(this)
            sAlertDialogBuilder.setTitle("Payment Confirmation")
            sAlertDialogBuilder.setMessage("Please Confirm your Payment")
            sAlertDialogBuilder.setCancelable(false)
            sAlertDialogBuilder.setPositiveButton("YES") { dialog, id ->
                val scanner = IntentIntegrator(this)
                scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
                scanner.setBeepEnabled(false)
                scanner.initiateScan()
            }
            sAlertDialogBuilder.setNegativeButton("NO") { dialog, id ->
                Toast.makeText(this, "OKay", Toast.LENGTH_SHORT).show()

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
            totalAmount -= 15
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
                val sharedPreferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("DRIVER_INFORMATION", result.contents)
                editor.apply()
                passData()
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
        val fareTotal = fare.text.toString().toInt()
        editor.putInt("FARE_TOTAL", fareTotal)
        editor.apply()
    }
}
