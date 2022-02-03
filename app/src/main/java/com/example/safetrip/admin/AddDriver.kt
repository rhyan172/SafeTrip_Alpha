package com.example.safetrip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.safetrip.database_adapter.AddDriver
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_driver.*

class AddDriver : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var addDriverName: EditText
    private lateinit var addDriverNum: EditText
    private lateinit var addDriverPlate:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_driver)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        addDriverName = findViewById(R.id.addDriverName)
        addDriverNum = findViewById(R.id.addDriverNum)
        addDriverPlate = findViewById(R.id.addDriverPlate)


        addDriverBtn.setOnClickListener{
            val driverName = addDriverName.text.toString().trim()
            val driverNumber = addDriverNum.text.toString().trim()
            val driverPlate = addDriverPlate.text.toString().trim()

            if(driverName.isEmpty())
            {
                Toast.makeText(this, "Please input Driver Name to add the driver.", Toast.LENGTH_LONG).show()
            }
            if(driverNumber.isEmpty())
            {
                Toast.makeText(this, "Please input Driver Number to add the driver.", Toast.LENGTH_LONG).show()
            }
            if(driverPlate.isEmpty())
            {
                Toast.makeText(this, "Please input Driver Plate Number to add the driver.", Toast.LENGTH_LONG).show()
            }
            else
            {
                addDriver()
                Toast.makeText(this, "Driver successfully added!", Toast.LENGTH_LONG).show()
                addDriverName.text.clear()
                addDriverNum.text.clear()
                addDriverPlate.text.clear()
            }
        }
    }

    private fun addDriver(){
        val driverName = addDriverName.text.toString().trim()
        val driverNumber = addDriverNum.text.toString().trim()
        val driverPlate = addDriverPlate.text.toString().trim()
        val driverPointsEarned = 0
        val driverProfit = "0.00"

        database = FirebaseDatabase.getInstance().getReference("Driver")
        val newDriver = AddDriver(driverName, "+63$driverNumber", driverPlate, driverPointsEarned, driverProfit)

        database.child("+63$driverNumber").setValue(newDriver)
    }
}