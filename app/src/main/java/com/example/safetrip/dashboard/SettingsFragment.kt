package com.example.safetrip

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_log_in_welcome.*
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment:Fragment(R.layout.fragment_settings) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val preferences = requireActivity().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        val sharedPreferences = requireActivity().getSharedPreferences("ONE_TIME_ACTIVITY", Context.MODE_PRIVATE)
        val nameFirst = preferences.getString("FIRST_NAME", "NULL").toString()
        val nameLast = preferences.getString("LAST_NAME", "NULL").toString()
        val PNumber = preferences.getString("PHONE_NUMBER", "NULL").toString()

        textViewName.text = "$nameFirst $nameLast"
        textViewNumber.text = "$PNumber"


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
            val intent = Intent(getActivity(), LoginSignup::class.java)
            val editor = sharedPreferences.edit()
            editor.putBoolean("ONE_TIME", false)
            editor.apply()
            Firebase.auth.signOut()
            startActivity(intent)
        }
    }
}