package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.icu.text.CurrencyPluralInfo
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.FeatureAdapter
import com.example.myapplication.model.FeatureItem
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : AppCompatActivity() {

    private lateinit var tvUserName: TextView
    private lateinit var tvUserEmail: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var dbHelper: DatabaseHelper
    private var userEmail: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        dbHelper = DatabaseHelper(this)
        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        userEmail = sharedPreferences.getString("user_email", null)

        tvUserName = findViewById(R.id.tvUserName)
        tvUserEmail = findViewById(R.id.tvUserEmail)

        if (userEmail.isNullOrEmpty()) {
            Toast.makeText(this, "Error: User email not found in session", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val userName: String? = dbHelper.getLoggedInUserName(userEmail)
        if (userName.isNullOrEmpty()) {
            Toast.makeText(this, "Error: User name not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        tvUserName.text = userName
        tvUserEmail.text = userEmail

        setupRecyclerView()

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

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        val features = listOf(
            FeatureItem("Dashboard", R.drawable.ic_dashboard),
            FeatureItem("Settings", R.drawable.ic_settings),
            FeatureItem("Currency Exchange", R.drawable.ic_transactions),
            FeatureItem("Transactions", R.drawable.ic_money)
        )

        recyclerView.adapter = FeatureAdapter(features) { item ->
            when (item.title) {
                "Dashboard" -> startActivity(Intent(this, DashboardActivity::class.java))
                "Settings" -> startActivity(Intent(this, SettingsActivity::class.java))
                "Currency Exchange" -> startActivity(Intent(this, CurrencyExchangeActivity::class.java))
                "Transactions" -> Toast.makeText(this, "To be Added", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
