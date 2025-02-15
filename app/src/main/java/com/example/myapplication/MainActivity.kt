package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)

        // Check if user is logged in
        val userEmail = sharedPreferences.getString("user_email", null)

        // Determine which activity to launch
        val intent = if (userEmail != null) {
            Intent(this, ProfileActivity::class.java)  // User is logged in → Go to HomeActivity
        } else {
            Intent(this, LoginActivity::class.java) // User is not logged in → Go to LoginActivity
        }

        startActivity(intent)
        finish() // Prevent returning to MainActivity on back press
    }
}
