package com.example.safetrip

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.safetrip.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment:Fragment(R.layout.fragment_settings) {

    private var firstName: String = "First Name"
    private var lastName: String = "Last Name"
    private var phnm: String = "Phone Number"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            val preferences = requireActivity().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
            val settingsP = preferences.getString("PHONE_NUMBER", "NULL").toString()
            val database = FirebaseDatabase.getInstance().getReference("Names")
            database.child(settingsP).get().addOnSuccessListener {
                val firstN = it.child("first").value
                val lastN = it.child("last").value
                val num = it.child("pnum").value

                firstName = firstN.toString()
                lastName = lastN.toString()
                phnm = num.toString()
            }

        viewSettingData()

        textViewAbout.setOnClickListener {
            val intent = Intent(getActivity(), About::class.java)
            getActivity()?.startActivity(intent)
        }

        textViewSettings.setOnClickListener {
            val intent = Intent(getActivity(), SettingsInfo::class.java)
            getActivity()?.startActivity(intent)
        }

        textViewFingerprint.setOnClickListener {
            val intent = Intent(getActivity(), Fingerprint::class.java)
            getActivity()?.startActivity(intent)
        }

        textViewFaqs.setOnClickListener {
            val intent = Intent(getActivity(), Faqs::class.java)
            getActivity()?.startActivity(intent)
        }

        logoutBtn.setOnClickListener {
            val sharedPreferences = requireActivity().getSharedPreferences("ONE_TIME_ACTIVITY", Context.MODE_PRIVATE)
            val intent = Intent(getActivity(), LoginSignup::class.java)
            val editor = sharedPreferences.edit()
            editor.putBoolean("ONE_TIME", false)
            editor.apply()
            Firebase.auth.signOut()
            startActivity(intent)
        }
    }

    fun viewSettingData(){
        var fullName = "$firstName $lastName"
        textViewName.text = fullName
        textViewNumber.text = phnm
    }

}