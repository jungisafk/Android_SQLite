package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnSignUp: Button
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var sharedPreferences: SharedPreferences



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbHelper = DatabaseHelper(this)
        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)

        etEmail = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnSignUp = findViewById(R.id.btnSignUp) // Initialize the Sign-Up button

        btnLogin.setOnClickListener { loginUser() }

        // Sign-Up button click listener
        btnSignUp.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        val db: SQLiteDatabase = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM users WHERE email=? AND password=?",
            arrayOf(email, password)
        )

        if (cursor.moveToFirst()) {
            // Get user ID from database
            val userId = dbHelper.getUserId(email)

            // Save login session with user ID
            sharedPreferences.edit().apply {
                putString("user_email", email)
                putInt("user_id", userId) // Store the user ID
                apply()
            }

            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()

            // Pass user ID to DashboardActivity
            val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Invalid Email or Password!", Toast.LENGTH_SHORT).show()
        }

        cursor.close()
    }

}
