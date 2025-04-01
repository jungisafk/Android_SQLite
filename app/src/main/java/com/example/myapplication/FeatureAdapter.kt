package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.FeatureItem
import com.google.android.material.card.MaterialCardView

class FeatureAdapter(
    private val featureList: List<FeatureItem>,
    private val onItemClick: (FeatureItem) -> Unit
) : RecyclerView.Adapter<FeatureAdapter.FeatureViewHolder>() {

    class FeatureViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: MaterialCardView = view.findViewById(R.id.cardView)  // Add this ID in your XML (if missing)
        val icon: ImageView = view.findViewById(R.id.settingsIcon)  // Use settingsIcon from XML
        val title: TextView = view.findViewById(R.id.settingsCard)  // Use settingsCard from XML
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_settings_card, parent, false)
        return FeatureViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {
        val item = featureList[position]
        holder.icon.setImageResource(item.iconResId)  // Use iconResId
        holder.title.text = item.title  // Use title
        holder.cardView.setOnClickListener { onItemClick(item) }  // Use cardView for click
    }

    override fun getItemCount(): Int = featureList.size
}
