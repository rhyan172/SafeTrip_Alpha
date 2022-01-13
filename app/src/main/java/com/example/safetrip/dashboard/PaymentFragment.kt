package com.example.safetrip

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.safetrip.R
import com.example.safetrip.dashboard.CashIn
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_payment.*

class PaymentFragment:Fragment(R.layout.fragment_payment) {

    private lateinit var database: DatabaseReference
    private lateinit var preferences: SharedPreferences
    private var paymentCredit: String = ""

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
                        val currentCredit = it.child("credits").value
                        paymentCredit = currentCredit.toString()
                        getView()?.findViewById<TextView>(R.id.textViewWalletPayment)?.text = paymentCredit
                    }
                }
            }
        })
        getView()?.findViewById<TextView>(R.id.textViewWalletPayment)?.text = paymentCredit
        gCashBtn.setOnClickListener(){
            val intent = Intent (activity, CashIn::class.java)
            activity?.startActivity(intent)
        }
    }
}