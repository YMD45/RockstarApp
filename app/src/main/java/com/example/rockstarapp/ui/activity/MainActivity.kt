package com.example.rockstarapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.rockstarapp.R
import com.example.rockstarapp.ui.fragment.BookmarkFragment
import com.example.rockstarapp.ui.fragment.ProfilFragment
import com.example.rockstarapp.ui.fragment.RockstarFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val rockstarFragment = RockstarFragment();
    private val profilFragment = ProfilFragment();
    private val bookmarkFragment = BookmarkFragment();


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(rockstarFragment)
        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.bookmarks -> replaceFragment(bookmarkFragment)
                R.id.profil -> replaceFragment(profilFragment)
                R.id.rockstar -> replaceFragment(rockstarFragment)
            }
            true
        }
    }

    /*
    * Replace the current fragment of the MainActivity,
    *
    * @param fragment the fragment to show
    *
    * @return void
    */
    private fun replaceFragment (fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container,fragment)
        transaction.commit()
    }
}