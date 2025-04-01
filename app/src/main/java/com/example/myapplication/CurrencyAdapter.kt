package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class CurrencyAdapter(private var rates: Map<String, Double>) :
    RecyclerView.Adapter<CurrencyAdapter.ViewHolder>() {

    private var currencyList = rates.toList()  // Convert map to list
    private var filteredList = currencyList.toList()  // Separate list for filtering

    // Update the entire dataset
    fun updateRates(newRates: Map<String, Double>) {
        rates = newRates
        currencyList = newRates.toList()
        filteredList = currencyList // Reset filtered list
        notifyDataSetChanged()
    }

    // Filter function
    fun filter(query: String) {
        filteredList = if (query.isEmpty()) {
            currencyList  // Show full list if search is empty
        } else {
            currencyList.filter { it.first.contains(query, ignoreCase = true) }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_currency, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (currency, rate) = filteredList[position]
        holder.bind(currency, rate)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val currencyName: TextView = itemView.findViewById(R.id.textViewCurrency)
        private val exchangeRate: TextView = itemView.findViewById(R.id.textViewRate)

        fun bind(currency: String, rate: Double) {
            currencyName.text = currency
            exchangeRate.text = rate.toString()
        }
    }
}
