package com.example.carchive.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.carchive.R

class PicturesAdapter(private val pictures: List<Int>) : RecyclerView.Adapter<PicturesAdapter.PictureViewHolder>() {

    // ViewHolder class to hold references to each item view
    class PictureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView2)
    }

    // Inflate the single picture layout and create a ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.slika_auta, parent, false)
        return PictureViewHolder(view)
    }

    // Bind each image to the ImageView in each item
    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        holder.imageView.setImageResource(pictures[position])
    }

    // Get the number of items in the list
    override fun getItemCount(): Int = pictures.size
}
