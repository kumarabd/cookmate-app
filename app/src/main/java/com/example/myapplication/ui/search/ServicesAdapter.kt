package com.example.myapplication.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class ServicesAdapter(private val services: List<String>, private val onItemClick: (service: String) -> Unit) : RecyclerView.Adapter<ServicesAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val serviceName: TextView = itemView.findViewById(R.id.serviceName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_service, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val service = services[position]
        holder.serviceName.text = service

        holder.itemView.setOnClickListener { onItemClick(service) }
    }

    override fun getItemCount(): Int {
        return services.size
    }
}
