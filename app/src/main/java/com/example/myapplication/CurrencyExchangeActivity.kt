package com.example.myapplication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
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
    private lateinit var searchCurrency: EditText

    private var allCurrencies: Map<String, Double> = emptyMap() // Store original data
    private val TAG = "CurrencyExchange"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_exchange)

        recyclerView = findViewById(R.id.recyclerViewCurrencies)
        recyclerView.layoutManager = LinearLayoutManager(this)

        searchCurrency = findViewById(R.id.searchCurrency)

        currencyAdapter = CurrencyAdapter(emptyMap())
        recyclerView.adapter = currencyAdapter

        fetchExchangeRates()

        // Add TextWatcher to filter results based on search input
        searchCurrency.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                currencyAdapter.filter(s.toString()) // Filter list dynamically
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun fetchExchangeRates() {
        Toast.makeText(this, "Loading currency rates...", Toast.LENGTH_SHORT).show()

        RetrofitClient.instance.getExchangeRates("PHP")
            .enqueue(object : Callback<ExchangeRateResponse> {
                override fun onResponse(call: Call<ExchangeRateResponse>, response: Response<ExchangeRateResponse>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody?.rates != null && responseBody.rates.isNotEmpty()) {
                            allCurrencies = responseBody.rates // Store full list
                            currencyAdapter.updateRates(allCurrencies)
                        } else {
                            Toast.makeText(this@CurrencyExchangeActivity, "No currency data available.", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this@CurrencyExchangeActivity, "Error: ${response.message()}", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ExchangeRateResponse>, t: Throwable) {
                    Toast.makeText(this@CurrencyExchangeActivity, "Failed to load currencies.", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun filterCurrencies(query: String) {
        val filteredRates = allCurrencies.filterKeys { it.contains(query, ignoreCase = true) }
        currencyAdapter.updateRates(filteredRates)
    }
}