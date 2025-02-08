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

class HomeActivity : AppCompatActivity() {
    private lateinit var tvWelcome: TextView
    private lateinit var btnLogout: Button
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var dashboardCard: CardView
    private lateinit var feature2Card: CardView
    private lateinit var feature3Card: CardView
    private lateinit var feature4Card: CardView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        dbHelper = DatabaseHelper(this)
        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)

        // Get stored user email from shared preferences
        val userEmail = sharedPreferences.getString("user_email", "Guest")!!

        // Initialize UI elements
        tvWelcome = findViewById(R.id.tvWelcome)
        btnLogout = findViewById(R.id.btnLogout)

        dashboardCard = findViewById(R.id.dashboardCard)
        feature2Card = findViewById(R.id.feature2Card)
        feature3Card = findViewById(R.id.feature3Card)
        feature4Card = findViewById(R.id.feature4Card)

        // Fetch user name from database
        val userName: String = dbHelper.getLoggedInUserName(userEmail)
        tvWelcome.text = "Welcome, $userName!"

        // Set click listeners for feature cards
        dashboardCard.setOnClickListener {  navigateToDashboard() }
        feature2Card.setOnClickListener {  navigateToFeature2() }
        feature3Card.setOnClickListener {  navigateToFeature3() }
        feature4Card.setOnClickListener {  navigateToFeature4() }

        // Logout button
        btnLogout.setOnClickListener { logoutUser() }
    }

    private fun navigateToDashboard() {
        Toast.makeText(this, "Dashboard Clicked!", Toast.LENGTH_SHORT).show()
    }

    private fun navigateToFeature2() {
        Toast.makeText(this, "Feature 2 Clicked!", Toast.LENGTH_SHORT).show()
    }

    private fun navigateToFeature3() {
        Toast.makeText(this, "Feature 3 Clicked!", Toast.LENGTH_SHORT).show()
    }

    private fun navigateToFeature4() {
        Toast.makeText(this, "Feature 4 Clicked!", Toast.LENGTH_SHORT).show()
    }

    private fun logoutUser() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        Toast.makeText(this, "Logged Out Successfully", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
        finish()
    }
}