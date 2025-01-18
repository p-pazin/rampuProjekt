package com.example.carchive.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.carchive.R
import com.example.carchive.entities.ExpandableItem

class ExpandableAdapter(private val itemList: List<ExpandableItem>) : RecyclerView.Adapter<ExpandableAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.expandable_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        holder.expandButton.text = item.buttonText

        holder.expandableContent.visibility = if (item.isExpanded) View.VISIBLE else View.GONE

        holder.expandableFirstText.text = item.expandableFirstText
        holder.expandableSecondText.text = item.expandableSecondText

        holder.expandButton.setOnClickListener {
            item.toggleExpanded()
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = itemList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val expandButton: Button = itemView.findViewById(R.id.expandButton)
        val expandableContent: LinearLayout = itemView.findViewById(R.id.expandableContent)
        val expandableFirstText: TextView = itemView.findViewById(R.id.expandableFirstText)
        val expandableSecondText: TextView = itemView.findViewById(R.id.expandableSecondText)
    }
}