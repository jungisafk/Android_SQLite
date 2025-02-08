package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SignupActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signupButton: Button
    private lateinit var loginRedirectText: TextView
    private lateinit var dbHelper: DatabaseHelper  // Initialize database helper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Initialize UI elements
        nameEditText = findViewById(R.id.nameEditText)  // Add this line
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        signupButton = findViewById(R.id.signupButton)
        loginRedirectText = findViewById(R.id.loginRedirectText)

        dbHelper = DatabaseHelper(this) // Initialize database helper

        // Sign up button click listener
        signupButton.setOnClickListener { handleSignup() }

        // Redirect to login
        loginRedirectText.setOnClickListener { navigateToLogin() }
    }

    private fun handleSignup() {
        val name = nameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (TextUtils.isEmpty(name)) {
            nameEditText.error = "Name is required"
            return
        }

        if (TextUtils.isEmpty(email)) {
            emailEditText.error = "Email is required"
            return
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.error = "Password is required"
            return
        }

        // Insert into database
        val success = dbHelper.insertUser(name, email, password)

        if (success) {
            Toast.makeText(this, "Signup Successful", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            Toast.makeText(this, "Signup Failed. Email might already exist.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
    }
}
