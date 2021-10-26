package com.example.safetrip.intro_slider

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.safetrip.LoginSignup
import com.example.safetrip.MainActivity
import com.example.safetrip.R
import kotlinx.android.synthetic.main.activity_intro_slider.*

class OnBoardScreen : AppCompatActivity() {
    private val fragmentList = ArrayList<Fragment>()

    //for one time intro slider
    lateinit var preference : SharedPreferences
    //this is the key for the one time intro slider
    val pref_intro = "Intro"

    //main method
    override fun onCreate(savedInstanceState: Bundle?) {
        //making the one time intro slider
        preference = getSharedPreferences("vpIntroSlider" , Context.MODE_PRIVATE)

        //flag for first time use. we use this code to prevent the intro slider from opening after first use
        if (!preference.getBoolean(pref_intro,true)) {
            startActivity(Intent(this, LoginSignup::class.java))
            finish()
        }

        super.onCreate(savedInstanceState)
        // making the status bar transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        setContentView(R.layout.activity_intro_slider)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val adapter = IntroSliderAdapter(this)
        vpIntroSlider.adapter = adapter
        fragmentList.addAll(listOf(
            Intro1Fragment(), Intro2Fragment(), Intro3Fragment()
        ))

        adapter.setFragmentList(fragmentList)
        indicatorLayout.setIndicatorCount(adapter.itemCount)
        indicatorLayout.selectCurrentPosition(0)
        registerListeners()
    }

    //action listener for fragments and buttons
    private fun registerListeners() {
        vpIntroSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                indicatorLayout.selectCurrentPosition(position)
                if (position < fragmentList.lastIndex) {
                    tvSkip.visibility = View.VISIBLE
                    tvNext.text = "Next"
                } else {
                    tvSkip.visibility = View.GONE
                    tvNext.text = "Get Started"
                }
            }
        })
        tvSkip.setOnClickListener {
            startActivity(Intent(this, LoginSignup::class.java))
            finish()
            //this will stop the intro slider next time when opening the app
            val editor = preference.edit()
            editor.putBoolean(pref_intro, false)
            editor.apply()
        }

        tvNext.setOnClickListener {
            val position = vpIntroSlider.currentItem
            if (position < fragmentList.lastIndex) {
                vpIntroSlider.currentItem = position + 1
            } else {
                startActivity(Intent(this, LoginSignup::class.java))
                finish()
                //this will stop the intro slider next time when opening the app
                val editor = preference.edit()
                editor.putBoolean(pref_intro, false)
                editor.apply()
            }
        }
    }
}