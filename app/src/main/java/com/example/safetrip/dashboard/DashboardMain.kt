package com.example.safetrip

import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.zxing.integration.android.IntentIntegrator
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

        getPointsData()

        /*redeemRewards.setOnClickListener(){
            redeemPoints()
        }*/
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment).commit()
        }

    private fun redeemPoints(){
        val sAlertDialogBuilder = AlertDialog.Builder(this)
        sAlertDialogBuilder.setTitle("Payment Confirmation")
        sAlertDialogBuilder.setMessage("Please Confirm your Payment")
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

    private fun getPointsData(){

    }
}

