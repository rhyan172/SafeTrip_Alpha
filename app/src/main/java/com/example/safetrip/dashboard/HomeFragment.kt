package com.example.safetrip

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment:Fragment(R.layout.fragment_home) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       rideBtn.setOnClickListener {
            //FirebaseAuth.getInstance().signOut()
           val intent = Intent (getActivity(), ScanPay::class.java)
           getActivity()?.startActivity(intent)
        }
    }
}

