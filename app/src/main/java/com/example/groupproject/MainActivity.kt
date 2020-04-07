package com.example.groupproject

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var nMainFrame: FrameLayout
    private lateinit var nMainNav:BottomNavigationView

    private lateinit var homeFragment:HomeFragment
    private lateinit var selectFragment:SelectFragment
    private lateinit var accountFragment:AccountFragment
    private lateinit var authorizationFragment: AuthorizationFragment

    private lateinit var pref: SharedPreferences
    private val APP_PREFERENCES = "appsettings"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nMainFrame = findViewById(R.id.main_frame)
        nMainNav = findViewById(R.id.main_nav)

        homeFragment = HomeFragment()
        selectFragment = SelectFragment()
        accountFragment = AccountFragment()
        authorizationFragment = AuthorizationFragment()

        pref = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

        setFragment(homeFragment)

        nMainNav.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
        when(it.itemId){
            R.id.nav_home -> {
                setFragment(homeFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_select -> {
                setFragment(selectFragment)
                return@OnNavigationItemSelectedListener  true
            }
            R.id.nav_account ->{
                setFragment(authorizationFragment)
                return@OnNavigationItemSelectedListener  true
            }
        }
        false
    }

    private fun setFragment(fragment: Fragment){
        val fragmentTransaction:FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_frame, fragment)
        fragmentTransaction.commit()
    }

}
