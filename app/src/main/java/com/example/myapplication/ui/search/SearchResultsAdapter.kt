package com.example.myapplication.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class SearchResultsAdapter(
    var items: List<String>,
    private val onItemClickListener: (String) -> Unit
) : RecyclerView.Adapter<SearchResultsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val resultTextView: TextView = itemView.findViewById(R.id.resultTextView)

        init {
            itemView.setOnClickListener {
                val item = items[adapterPosition]
                onItemClickListener(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.resultTextView.text = items[position]
    }

    override fun getItemCount(): Int = items.size
}
