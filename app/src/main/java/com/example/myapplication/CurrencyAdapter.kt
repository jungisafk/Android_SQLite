package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CurrencyAdapter(private var rates: Map<String, Double>) :
    RecyclerView.Adapter<CurrencyAdapter.ViewHolder>() {

    private var currencyList = rates.toList()  // Convert map to list

    // Add this method to update data without recreating the adapter
    fun updateRates(newRates: Map<String, Double>) {
        rates = newRates
        currencyList = newRates.toList()  // Update the list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_currency, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (currency, rate) = currencyList[position]
        holder.bind(currency, rate)
    }

    override fun getItemCount(): Int {
        return currencyList.size
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