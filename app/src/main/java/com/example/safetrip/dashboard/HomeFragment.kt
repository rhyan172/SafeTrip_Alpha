package com.example.safetrip

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.safetrip.R
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment:Fragment(R.layout.fragment_home) {

    private var credit: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val preferences = requireActivity().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        val homeP = preferences.getString("PHONE_NUMBER", "NULL").toString()



        val database = FirebaseDatabase.getInstance().getReference("Names")
        database.child(homeP).get().addOnSuccessListener {
            val cred = it.child("credits").value
            credit = cred.toString()
        }

        viewHomeData()

        rideBtn.setOnClickListener {
            //FirebaseAuth.getInstance().signOut()
            val intent = Intent (activity, ScanPay::class.java)
            activity?.startActivity(intent)
        }
    }

    fun viewHomeData(){
        textViewWalletHome.text = credit
    }

}

