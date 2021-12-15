package com.example.safetrip

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.safetrip.R
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_payment.*
import kotlinx.android.synthetic.main.fragment_rewards.*

class RewardsFragment:Fragment(R.layout.fragment_rewards) {

    private var points: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val preferences = requireActivity().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        val rewardsP = preferences.getString("PHONE_NUMBER", "NULL").toString()

        val database = FirebaseDatabase.getInstance().getReference("Names")
        database.child(rewardsP).get().addOnSuccessListener {
            val pnts = it.child("points").value
            points = pnts.toString()
        }
        viewRewardData()

    }

    fun viewRewardData() {
        textViewPoints.text = points
    }
}