package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 1;
    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT UNIQUE, password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    // Insert a new user
    public boolean insertUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("password", password); // Store hashed password in production

        long result = db.insert("users", null, values);
        return result != -1;  // Returns true if insert was successful
    }

    // Validate login credentials & return user ID
    public int validateUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM users WHERE email=? AND password=?", new String[]{email, password});

        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);

            // Save user email in SharedPreferences after successful login
            SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("USER_EMAIL", email);
            editor.apply();
        }
        cursor.close();
        return userId; // Returns -1 if login fails
    }

    // Get user's name from the database
    public String getLoggedInUserName(String email) {
        if (email == null || email.isEmpty()) {
            return "Unknown User";
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM users WHERE email=?", new String[]{email});

        String userName = "Unknown User"; // Default value in case no user is found

        if (cursor.moveToFirst()) {
            userName = cursor.getString(0);
        }

        cursor.close();
        db.close();

        return userName;
    }


    // Get user ID based on email
    public int getUserId(String email) {
        if (email == null) {
            return -1;
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM users WHERE email=?", new String[]{email});

        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }
        cursor.close();
        return userId;
    }


    // Update user details
    public boolean updateUser(int id, String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("email", email);

        if (password != null && !password.isEmpty()) {
            values.put("password", password);
        }

        int result = db.update("users", values, "id=?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;  // Return false if no rows were updated
    }


    // Delete user account
    public boolean deleteUser(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("users", "id=?", new String[]{String.valueOf(id)});

        db.close();
        return result > 0;  // Return false if deletion fails
    }


    public boolean updateUserWithoutPassword(int userId, String newName, String newEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", newName);
        values.put("email", newEmail);

        int rowsAffected = db.update("users", values, "id=?", new String[]{String.valueOf(userId)});
        db.close();
        return rowsAffected > 0;
    }

}
