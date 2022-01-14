package com.example.safetrip

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.safetrip.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_rewards.*
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment:Fragment(R.layout.fragment_settings) {

    private lateinit var database: DatabaseReference
    private lateinit var preferences: SharedPreferences
    private var settingFirstName: String = ""
    private var settingLastName: String = ""
    private var settingPhoneNumber: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferences = requireActivity().getSharedPreferences("SHARED_PREF", AppCompatActivity.MODE_PRIVATE)

        database = FirebaseDatabase.getInstance().getReference("Names")
        database.addValueEventListener(object: ValueEventListener {

            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {

                val phoneNumberHome = preferences.getString("PHONE_NUMBER", "NULL")
                database = FirebaseDatabase.getInstance().reference
                database.child("Names/$phoneNumberHome").get().addOnSuccessListener {
                    if(it.exists()){
                        val firstName = it.child("first").value
                        val lastName = it.child("last").value
                        val phnm = it.child("pnum").value

                        settingFirstName = firstName.toString()
                        settingLastName = lastName.toString()
                        settingPhoneNumber = phnm.toString()

                        val fullName = "$settingFirstName $settingLastName"
                        getView()?.findViewById<TextView>(R.id.textViewName)?.text = fullName
                        getView()?.findViewById<TextView>(R.id.textViewNumber)?.text = settingPhoneNumber
                    }
                }
            }
        })
        val fullName = "$settingFirstName $settingLastName"
        getView()?.findViewById<TextView>(R.id.textViewName)?.text = fullName
        getView()?.findViewById<TextView>(R.id.textViewNumber)?.text = settingPhoneNumber

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
            editor.putBoolean("SWITCH_KEY", false)
            editor.apply()
            Firebase.auth.signOut()
            startActivity(intent)
            requireActivity().finish()
        }
    }
}