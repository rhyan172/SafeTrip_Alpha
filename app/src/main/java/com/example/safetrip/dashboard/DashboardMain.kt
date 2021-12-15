package com.example.safetrip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.safetrip.R
import kotlinx.android.synthetic.main.activity_dashboard_main.*

class DashboardMain : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_main)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        val firstFragment = HomeFragment()
        val secondFragment = PaymentFragment()
        val thirdFragment = RewardsFragment()
        val fourthFragment = SettingsFragment()

        setCurrentFragment(firstFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> setCurrentFragment(firstFragment)
                R.id.payment ->setCurrentFragment(secondFragment)
                R.id.reward ->setCurrentFragment(thirdFragment)
                R.id.settings ->setCurrentFragment(fourthFragment)

            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment).commit()
        }
    }
