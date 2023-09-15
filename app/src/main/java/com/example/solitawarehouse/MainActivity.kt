package com.example.solitawarehouse

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.navigation.NavigationView
import model.User
import androidx.navigation.fragment.findNavController

class MainActivity : AppCompatActivity() {
    lateinit var toggle : ActionBarDrawerToggle
    lateinit var drawerLayout : DrawerLayout
    lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        // Get the NavController from the NavHostFragment
        navController = navHostFragment.navController

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {

            when(it.itemId) {
                R.id.nav_loans -> replaceFragment(R.id.action_to_loansFragment, it.title.toString())
                R.id.nav_rentitem -> replaceFragment(R.id.action_to_rentItem, it.title.toString())
                R.id.nav_returnitem -> replaceFragment(R.id.action_to_returnItem, it.title.toString())
                R.id.nav_itemavailability -> replaceFragment(R.id.action_to_itemAvailability, it.title.toString())
                R.id.nav_reports -> replaceFragment(R.id.action_to_reports, it.title.toString())
                R.id.nav_logout -> logOut(R.id.action_to_login)
            }; true
        }

    }

    private fun replaceFragment(fragment: Int, title: String) {
        navController.navigate(fragment)
        drawerLayout.closeDrawers()

        setTitle(title)
    }

    private fun logOut(fragment: Int) {
        Log.i("","Logged out")
        navController.navigate(fragment)
        drawerLayout.closeDrawers()

        setTitle("Login")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return false
    }

}
