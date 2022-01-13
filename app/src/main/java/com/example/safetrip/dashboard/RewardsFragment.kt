package com.example.safetrip

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.safetrip.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_payment.*
import kotlinx.android.synthetic.main.fragment_rewards.*

class RewardsFragment:Fragment(R.layout.fragment_rewards) {

    private lateinit var database: DatabaseReference
    private lateinit var preferences: SharedPreferences
    private var pointsOfUser: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferences = requireActivity().getSharedPreferences("SHARED_PREF", AppCompatActivity.MODE_PRIVATE)
        val phoneNumberHome = preferences.getString("PHONE_NUMBER", "NULL")

        database = FirebaseDatabase.getInstance().getReference("Names")
        database.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {

                database = FirebaseDatabase.getInstance().reference
                database.child("Names/$phoneNumberHome").get().addOnSuccessListener {
                    if(it.exists()){
                        val currentPoints: Any? = it.child("points").value
                        pointsOfUser = currentPoints.toString()
                        getView()?.findViewById<TextView>(R.id.textViewPoints)?.text = pointsOfUser
                    }
                }
            }
        })
        getView()?.findViewById<TextView>(R.id.textViewPoints)?.text = pointsOfUser
        redeemRewards.setOnClickListener(){
            (activity as DashboardMain).getPointsData()
            (activity as DashboardMain).passDataPoints()
            (activity as DashboardMain).redeemPoints()
        }
    }
}