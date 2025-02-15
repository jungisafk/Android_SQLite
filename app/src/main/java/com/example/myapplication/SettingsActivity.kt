package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class SettingsActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private var userEmail: String? = null
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Use correct SharedPreferences key
        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        userEmail = sharedPreferences.getString("user_email", null)  // Fix key name

        // Initialize DatabaseHelper
        dbHelper = DatabaseHelper(this)

        val btnUpdateAccount = findViewById<Button>(R.id.btnUpdateAccount)
        val btnDeleteAccount = findViewById<Button>(R.id.btnDeleteAccount)

        btnUpdateAccount.setOnClickListener {
            openUpdateAccountScreen()
        }

        btnDeleteAccount.setOnClickListener {
            confirmDeleteAccount()
        }

        // Bottom Navigation
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

    private fun openUpdateAccountScreen() {
        if (userEmail.isNullOrEmpty()) {
            Toast.makeText(this, "Error: User email not found!", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = dbHelper.getUserId(userEmail!!)
        if (userId == -1) {
            Toast.makeText(this, "Error: User ID not found!", Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(this, UpdateAccountActivity::class.java)
        intent.putExtra("user_id", userId)
        startActivity(intent)
    }

    private fun confirmDeleteAccount() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Account")
        builder.setMessage("Are you sure you want to delete your account? This action cannot be undone.")
        builder.setPositiveButton("Delete") { _, _ -> deleteAccount() }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun deleteAccount() {
        if (userEmail.isNullOrEmpty()) {
            Toast.makeText(this, "Error: User email not found!", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = dbHelper.getUserId(userEmail!!)
        if (userId == -1) {
            Toast.makeText(this, "Error: User ID not found!", Toast.LENGTH_SHORT).show()
            return
        }

        val isDeleted = dbHelper.deleteUser(userId)

        if (isDeleted) {
            Toast.makeText(this, "Account deleted successfully!", Toast.LENGTH_SHORT).show()
            sharedPreferences.edit().clear().apply()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Error: Account deletion failed!", Toast.LENGTH_SHORT).show()
        }
    }
}
