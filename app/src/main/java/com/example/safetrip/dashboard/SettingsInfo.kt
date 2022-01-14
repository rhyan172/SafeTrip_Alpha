package com.example.safetrip

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.safetrip.dashboard.SafeTripLocation
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsInfo : AppCompatActivity() {

    private lateinit var preferences: SharedPreferences
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        val txtNameEdit = findViewById<TextView>(R.id.txtEditInfo)
        txtNameEdit.setOnClickListener {
            val intent = Intent(this, EditInfo::class.java)
            startActivity(intent)
        }

        val txtPinEdit = findViewById<TextView>(R.id.txtEditPin)
        txtPinEdit.setOnClickListener {
            val intent = Intent(this, EditPin::class.java)
            startActivity(intent)
        }

        deactivateBtn.setOnClickListener{
            val sAlertDialogBuilder = AlertDialog.Builder(this)
            sAlertDialogBuilder.setTitle("Account Deactivation")
            sAlertDialogBuilder.setMessage("Are you sure you want to deactivate your account?")
            sAlertDialogBuilder.setCancelable(false)
            sAlertDialogBuilder.setPositiveButton("YES") { dialog, id ->
                preferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
                val phn = preferences.getString("PHONE_NUMBER","NULL")
                database = FirebaseDatabase.getInstance().reference
                database.child("Names/$phn").removeValue()
                Toast.makeText(this, "Account deactivated. Thank you for using SafeTrip!", Toast.LENGTH_LONG).show()
                val sharedPreferences = getSharedPreferences("ONE_TIME_ACTIVITY", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putBoolean("ONE_TIME", false)
                editor.putBoolean("SWITCH_KEY", false)
                editor.apply()
                startActivity(Intent(this, LoginSignup::class.java))
                finish()
            }
            sAlertDialogBuilder.setNegativeButton("NO") { dialog, id ->
                Toast.makeText(this, "Deactivation cancelled", Toast.LENGTH_SHORT).show()

            }
            val sAlertDialog = sAlertDialogBuilder.create()
            sAlertDialog.show()
        }
    }
}