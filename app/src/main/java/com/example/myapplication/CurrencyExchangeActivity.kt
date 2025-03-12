package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CurrencyExchangeActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var currencyAdapter: CurrencyAdapter
    private val TAG = "CurrencyExchange"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_exchange)

        recyclerView = findViewById(R.id.recyclerViewCurrencies)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize adapter with empty list first
        currencyAdapter = CurrencyAdapter(emptyMap())
        recyclerView.adapter = currencyAdapter

        fetchExchangeRates()
    }

    private fun fetchExchangeRates() {
        Toast.makeText(this, "Loading currency rates...", Toast.LENGTH_SHORT).show()

        RetrofitClient.instance.getExchangeRates("USD")
            .enqueue(object : Callback<ExchangeRateResponse> {
                override fun onResponse(call: Call<ExchangeRateResponse>, response: Response<ExchangeRateResponse>) {
                    Log.d(TAG, "Raw response: ${response.raw()}")

                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        Log.d(TAG, "Full Response: $responseBody")

                        // Check if the response has valid data
                        if (responseBody?.rates != null && responseBody.rates.isNotEmpty()) {
                            currencyAdapter.updateRates(responseBody.rates)
                            Log.d(TAG, "Loaded ${responseBody.rates.size} currencies")
                        } else {
                            // If rates is null or empty
                            Log.e(TAG, "API returned empty data: $responseBody")
                            Toast.makeText(
                                this@CurrencyExchangeActivity,
                                "Error loading currencies: No data returned",
                                Toast.LENGTH_LONG
                            ).show()

                        }
                    } else {
                        val errorMsg = "Error: ${response.code()} - ${response.message()}"
                        val errorBody = response.errorBody()?.string() ?: "No error details"
                        Log.e(TAG, "Response unsuccessful: $errorMsg, $errorBody")
                        Toast.makeText(this@CurrencyExchangeActivity, errorMsg, Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ExchangeRateResponse>, t: Throwable) {
                    Log.e(TAG, "API call failed: ${t.message}", t)
                    Toast.makeText(
                        this@CurrencyExchangeActivity,
                        "Failed to load currencies: ${t.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()

                }
            })
    }
}