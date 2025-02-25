package com.example.carchive.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.carchive.R

class PicturesAdapter(
    private val pictures: MutableList<Uri>,
    private val longClickListener: (Uri) -> Unit
) : RecyclerView.Adapter<PicturesAdapter.PictureViewHolder>() {

    class PictureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.car_picture, parent, false)
        return PictureViewHolder(view)
    }

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        val imageUri = pictures[position]

        Glide.with(holder.itemView.context)
            .load(imageUri)
            .into(holder.imageView)

        holder.imageView.setOnLongClickListener {
            longClickListener(imageUri)
            true
        }
    }

    override fun getItemCount(): Int = pictures.size

    fun updateData(newPictures: List<Uri>) {
        pictures.clear()
        pictures.addAll(newPictures)
        notifyDataSetChanged()
    }
}
