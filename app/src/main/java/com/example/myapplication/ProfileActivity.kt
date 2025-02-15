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

class ProfileActivity : AppCompatActivity() {

    private lateinit var tvUserName: TextView
    private lateinit var tvUserEmail: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var dbHelper: DatabaseHelper
    private var userEmail: String? = null // Store user email instead of ID

    private lateinit var dashboardCard: CardView
    private lateinit var settingsCard: CardView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        dbHelper = DatabaseHelper(this)
        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        userEmail = sharedPreferences.getString("user_email", null) // Use "user_email"

        // Initialize UI elements
        tvUserName = findViewById(R.id.tvUserName)
        tvUserEmail = findViewById(R.id.tvUserEmail)
        dashboardCard = findViewById(R.id.dashboardCard)
        settingsCard = findViewById(R.id.SettingsCard)

        // Ensure userEmail is not null
        if (userEmail.isNullOrEmpty()) {
            Toast.makeText(this, "Error: User email not found in session", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Fetch user name from database
        val userName: String? = dbHelper.getLoggedInUserName(userEmail)
        if (userName.isNullOrEmpty()) {
            Toast.makeText(this, "Error: User name not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        tvUserName.text = userName
        tvUserEmail.text = userEmail // Use the email directly from SharedPreferences

        // Set click listeners for feature cards
        dashboardCard.setOnClickListener { navigateToDashboard() }
        settingsCard.setOnClickListener { navigateToSettings() }

        val btnLogout = findViewById<Button>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            logoutUser()
        }

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

    private fun navigateToDashboard() {
        Toast.makeText(this, "Dashboard Clicked!", Toast.LENGTH_SHORT).show()
        val intent = Intent(this@ProfileActivity, DashboardActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToSettings() {
        Toast.makeText(this, "Settings Clicked!", Toast.LENGTH_SHORT).show()
        val intent = Intent(this@ProfileActivity, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun logoutUser() {
        sharedPreferences.edit().clear().apply()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
