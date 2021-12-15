package com.example.safetrip

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.safetrip.R
import com.example.safetrip.dashboard.CashIn
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_payment.*

class PaymentFragment:Fragment(R.layout.fragment_payment) {

    private var creditPayment: String = "0"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val preferences = requireActivity().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        val paymentP = preferences.getString("PHONE_NUMBER", "NULL").toString()

        val database = FirebaseDatabase.getInstance().getReference("Names")
        database.child(paymentP).get().addOnSuccessListener {
            val credPayment = it.child("credits").value
            creditPayment = credPayment.toString()
        }
        viewPaymentData()

        gCashBtn.setOnClickListener(){
            val intent = Intent (activity, CashIn::class.java)
            activity?.startActivity(intent)
        }
    }

    fun viewPaymentData(){
        textViewWalletPayment.text = creditPayment
    }
}