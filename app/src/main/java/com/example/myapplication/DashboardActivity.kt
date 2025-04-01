package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.content.SharedPreferences
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.myapplication.R
import android.widget.TextView

class DashboardActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var featureCardAdapter: FeatureCardAdapter
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var tvWelcome: TextView

    private var userEmail: String? = null // Store user email instead of ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        dbHelper = DatabaseHelper(this)

        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)

        // Get stored user email from shared preferences
        val userEmail = sharedPreferences.getString("user_email", "Guest")!!
        tvWelcome = findViewById(R.id.tvWelcome) // Initialize the TextView

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)  // 2 columns
        featureCardAdapter = FeatureCardAdapter(getFeatureCards())  // Create adapter with data
        recyclerView.adapter = featureCardAdapter

        // Fetch user name from database
        val userName: String = dbHelper.getLoggedInUserName(userEmail) // Ensure this method returns the correct username
        tvWelcome.text = "Welcome, $userName!" // Set welcome message with the username

        // Initialize Bottom Navigation
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.setOnItemSelectedListener { item ->
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

    private fun getFeatureCards(): List<FeatureCard> {
        // This is where you provide the data for the feature cards
        return listOf(
            FeatureCard("Profile", R.drawable.ic_profile), // Make sure you have the correct resource
            FeatureCard("Currency Exchange", R.drawable.ic_transactions),
            FeatureCard("Settings", R.drawable.ic_settings)
        )
    }
}

// FeatureCard data class
data class FeatureCard(val title: String, val iconResId: Int)

// Adapter for RecyclerView
class FeatureCardAdapter(private val featureCardList: List<FeatureCard>) :
    RecyclerView.Adapter<FeatureCardAdapter.FeatureCardViewHolder>() {

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): FeatureCardViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.item_feature_card, parent, false)
        return FeatureCardViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeatureCardViewHolder, position: Int) {
        val featureCard = featureCardList[position]
        holder.featureTitle.text = featureCard.title
        holder.featureIcon.setImageResource(featureCard.iconResId)

        // Set click listener on the CardView
        holder.itemView.setOnClickListener {
            when (featureCard.title) {
                "Profile" -> {
                    val intent = android.content.Intent(it.context, ProfileActivity::class.java)
                    it.context.startActivity(intent)
                }
                "Currency Exchange" -> {
                    val intent = android.content.Intent(it.context, CurrencyExchangeActivity::class.java)
                    it.context.startActivity(intent)
                }
                "Settings" -> {
                    val intent = android.content.Intent(it.context, SettingsActivity::class.java)
                    it.context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int = featureCardList.size

    inner class FeatureCardViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val featureTitle: android.widget.TextView = view.findViewById(R.id.featureTitle)
        val featureIcon: android.widget.ImageView = view.findViewById(R.id.featureIcon)
    }
}
