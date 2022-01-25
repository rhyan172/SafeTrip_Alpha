package com.example.safetrip

import android.annotation.SuppressLint
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
import kotlinx.android.synthetic.main.activity_scan_pay.textViewPeso
import java.text.SimpleDateFormat
import java.util.*

class ScanPay : AppCompatActivity() {
    private lateinit var fare: TextView
    private lateinit var database: DatabaseReference
    private var totalAmount: Float = 0.00F
    private lateinit var preferences: SharedPreferences
    private var numberOfPassenger: Int = 0
    private var currentUserPoints: Int = 0
    private var earnedPoints: Int = 0
    private var currentCreditUser: Float = 0.00F
    private var transactNumber: Int = 0
    private var farePrice: Float = 0.00F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_pay)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        fare = findViewById(R.id.textViewTotal)

        preferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        val pnc = preferences.getString("PHONE_NUMBER", "PHONE-NUMBER").toString()

        database = FirebaseDatabase.getInstance().reference
        database.child("Fare").get().addOnSuccessListener {
            if(it.exists()){
                val priceFare = it.child("price").value
                farePrice = priceFare.toString().toFloat()
                fare.text = farePrice.toString()

            }
        }
        database.child("Names/$pnc").get().addOnSuccessListener {
            if(it.exists())
            {
                val userCredit = it.child("credits").value
                currentCreditUser = userCredit.toString().toFloat()

                val userPoints = it.child("points").value
                currentUserPoints = userPoints.toString().toInt()

                val transactionNumber = it.child("transact").value
                if(transactionNumber == null){
                    transactNumber = 1
                }
                else{
                    transactNumber = transactionNumber.toString().toInt()
                }
            }
        }
        database.child("Fare").get().addOnSuccessListener {
            if(it.exists()){
                val priceFare = it.child("price").value
                farePrice = priceFare.toString().toFloat()
                fare.text = farePrice.toString()
            }
        }
        database.child("Points").get().addOnSuccessListener {
            val earnPoints = it.child("ePoints").value
            earnedPoints = earnPoints.toString().toInt()
        }

        var increment = 1
        fare.text = farePrice.toString()


        scanPay.setOnClickListener() {
            numberOfPassenger = numPassenger.text.toString().toInt()

            val sAlertDialogBuilder = AlertDialog.Builder(this)
            sAlertDialogBuilder.setTitle("Payment Confirmation")
            sAlertDialogBuilder.setMessage("Please Confirm your Payment")
            sAlertDialogBuilder.setCancelable(false)
            sAlertDialogBuilder.setPositiveButton("YES") { dialog, id ->
                if(totalAmount > currentCreditUser){
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
            totalAmount = (farePrice * increment)
            numPassenger.setText(increment.toString())
            fare.text = totalAmount.toString()
        }
        minusBtn.setOnClickListener(){
            increment--
            totalAmount -= farePrice
            numPassenger.setText(increment.toString())
            fare.text = totalAmount.toString()
        }
    }

    @SuppressLint("SimpleDateFormat")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result: IntentResult =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            preferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
            val driverId = result.contents
            database.child("Driver/$driverId").get().addOnSuccessListener {
                if(it.exists())
                {
                    val pnc = preferences.getString("PHONE_NUMBER", "PHONE-NUMBER").toString()

                    val nameOfDriver = it.child("driverName").value
                    val profitDriver = it.child("driverProfit").value
                    val pd = profitDriver.toString().toFloat()

                    if (result.contents == null)
                    {
                        Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        val totalPay = currentCreditUser - fare.text.toString().toFloat()
                        val driverProfit = pd + fare.text.toString().toFloat()
                        val ep = currentUserPoints + earnedPoints
                        val transactionCounter = transactNumber + 1

                        val database = FirebaseDatabase.getInstance().reference
                        database.child("Names/$pnc/credits").setValue(totalPay.toString())
                        database.child("Names/$pnc/points").setValue(ep)
                        database.child("Names/$pnc/transact").setValue(transactionCounter)
                        database.child("Driver/$driverId/driverProfit").setValue(driverProfit.toString())

                        //for transaction history
                        val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss")
                        val currentDate: String = simpleDateFormat.format(Date())

                        database.child("Names/$pnc/TransactionHistory/$transactNumber/transactionNumber").setValue(transactNumber)
                        database.child("Names/$pnc/TransactionHistory/$transactNumber/driverName").setValue(nameOfDriver)
                        database.child("Names/$pnc/TransactionHistory/$transactNumber/totalFare").setValue(fare.text.toString())
                        database.child("Names/$pnc/TransactionHistory/$transactNumber/dateAndTime").setValue(currentDate)

                        startActivity(Intent(this, SafeTripLocation::class.java))
                        Toast.makeText(this, "Scanned Success", Toast.LENGTH_LONG).show()
                        finish()
                    }
                }
                else
                {
                    Toast.makeText(this, "Invalid QR Code", Toast.LENGTH_SHORT).show()
                }
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
