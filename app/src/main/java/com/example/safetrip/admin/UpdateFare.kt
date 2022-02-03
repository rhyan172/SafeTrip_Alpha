package com.example.safetrip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_update_fare.*

class UpdateFare : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private var fare: String = ""
    private var points: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_fare)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)
        getFare()

        updateFareBtn.setOnClickListener{
            updateFare()
        }

        updatePointsBtn.setOnClickListener{
            updatePoints()
        }
    }

    private fun updateFare(){
        val newFare = editFare.text.toString().toFloat()

        database = FirebaseDatabase.getInstance().reference
        database.child("Fare/price").setValue(newFare.toString())
        Toast.makeText(this, "Fare updated!", Toast.LENGTH_SHORT).show()
    }

    private fun updatePoints(){
        val newPoints = editPoints.text.toString().toInt()

        database = FirebaseDatabase.getInstance().reference
        database.child("Fare/reward").setValue(newPoints)
        Toast.makeText(this, "Points updated!", Toast.LENGTH_SHORT).show()
    }

    private fun getFare(){
        database = FirebaseDatabase.getInstance().reference
        database.child("Fare").get().addOnSuccessListener {
            if(it.exists())
            {
                val oldFare = it.child("price").value
                val oldPoints = it.child("reward").value

                fare = oldFare.toString()
                points = oldPoints.toString().toInt()

                editFare.setText(fare)
                editPoints.setText(points.toString())
            }
        }
    }
}