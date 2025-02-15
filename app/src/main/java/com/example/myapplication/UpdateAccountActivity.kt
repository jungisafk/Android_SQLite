package com.example.myapplication

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class UpdateAccountActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSave: Button
    private var userId: Int = -1  // Default invalid user ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_account)

        dbHelper = DatabaseHelper(this)
        etUsername = findViewById(R.id.etUsername)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnSave = findViewById(R.id.btnSave)

        // Retrieve user ID from intent
        userId = intent.getIntExtra("user_id", -1)
        if (userId == -1) {
            Toast.makeText(this, "User ID not found!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        loadUserData(userId)

        btnSave.setOnClickListener {
            updateUserAccount()
        }
    }

    private fun loadUserData(userId: Int) {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE id=?", arrayOf(userId.toString()))

        if (!cursor.moveToFirst()) {
            Toast.makeText(this, "User data not found!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        etUsername.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")))
        etEmail.setText(cursor.getString(cursor.getColumnIndexOrThrow("email")))
        cursor.close()
    }

    private fun updateUserAccount() {
        val newUsername = etUsername.text.toString().trim()
        val newEmail = etEmail.text.toString().trim()
        val newPassword = etPassword.text.toString().trim()

        if (newUsername.isEmpty() || newEmail.isEmpty()) {
            Toast.makeText(this, "Username and Email are required", Toast.LENGTH_SHORT).show()
            return
        }

        val isUpdated = if (newPassword.isNotEmpty()) {
            dbHelper.updateUser(userId, newUsername, newEmail, newPassword)
        } else {
            dbHelper.updateUserWithoutPassword(userId, newUsername, newEmail)
        }

        if (isUpdated) {
            Toast.makeText(this, "Account updated successfully", Toast.LENGTH_SHORT).show()

            val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                putString("USER_EMAIL", newEmail) // Fixed key name
                apply()
            }

            finish()
        } else {
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
        }
    }
}
