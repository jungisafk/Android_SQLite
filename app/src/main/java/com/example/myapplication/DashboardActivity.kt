package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardActivity : AppCompatActivity() {
    private lateinit var tvWelcome: TextView
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var sharedPreferences: SharedPreferences
    private var userEmail: String? = null // Store user email instead of ID

    private lateinit var profileCard: CardView
    private lateinit var settingsCard: CardView
    private lateinit var feature3Card: CardView
    private lateinit var feature4Card: CardView

    @SuppressLint("SetTextI18n", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        dbHelper = DatabaseHelper(this)

        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)

        // Get stored user email from shared preferences
        val userEmail = sharedPreferences.getString("user_email", "Guest")!!

        // Initialize UI elements
        tvWelcome = findViewById(R.id.tvWelcome)
        settingsCard = findViewById(R.id.SettingsCard)

        profileCard = findViewById(R.id.profileCard)
        settingsCard = findViewById(R.id.SettingsCard)
        feature3Card = findViewById(R.id.feature3Card)
        feature4Card = findViewById(R.id.feature4Card)

        // Fetch user name from database
        val userName: String = dbHelper.getLoggedInUserName(userEmail)
        tvWelcome.text = "Welcome, $userName!"

        // Set click listeners for feature cards
        profileCard.setOnClickListener {  navigateToProfile() }
        settingsCard.setOnClickListener {  navigateToSettings() }
        feature3Card.setOnClickListener {  navigateToFeature3() }
        feature4Card.setOnClickListener {  navigateToFeature4() }

        // Initialize Bottom Navigation
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigation?.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> {
                    startActivity(Intent(this, DashboardActivity::class.java))
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                R.id.nav_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun navigateToProfile() {
        Toast.makeText(this, "Profile Clicked!", Toast.LENGTH_SHORT).show()
        val intent = Intent(this@DashboardActivity, ProfileActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToSettings() {
        Toast.makeText(this, "Dashboard Clicked!", Toast.LENGTH_SHORT).show()
        val intent = Intent(this@DashboardActivity, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToFeature3() {
        Toast.makeText(this, "Feature 3 Clicked!", Toast.LENGTH_SHORT).show()
    }

    private fun navigateToFeature4() {
        Toast.makeText(this, "Feature 4 Clicked!", Toast.LENGTH_SHORT).show()
    }
}