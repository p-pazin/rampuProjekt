package com.example.carchive.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.carchive.R
import com.example.carchive.entities.Ad
import com.example.carchive.entities.Contact
import com.example.carchive.entities.Vehicle

class AdsAdapter(
    var adsData : List<Ad>,
    private val onAdClick:(Ad) -> Unit) : RecyclerView.Adapter<AdsAdapter.AdViewHolder>() {

    inner class AdViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        private val adTitle: TextView
        private val ID: TextView
        private val adDescription: TextView
        private val adBrand: TextView
        private val adModel: TextView
        private val adPaymentMethod: TextView
        private val adDate: TextView
        private val adPicture : ImageView

        init {
            adTitle = view.findViewById(R.id.tv_ad_title)
            ID = view.findViewById(R.id.tv_ad_id)
            adDescription = view.findViewById(R.id.tv_ad_description)
            adBrand = view.findViewById(R.id.tv_ad_brand)
            adModel = view.findViewById(R.id.tv_ad_model)
            adPaymentMethod = view.findViewById(R.id.tv_ad_payment_method)
            adDate = view.findViewById(R.id.tv_ad_date)
            adPicture = view.findViewById(R.id.iv_ad_picture)
        }
        fun bind(ad: Ad) {
            adTitle.text = ad.title
            ID.text = "ID: " + ad.id
            adDescription.text = ad.description
            adBrand.text = ad.brand
            adModel.text = ad.model
            adPaymentMethod.text = ad.paymentMethod
            adDate.text = ad.dateOfPublishment

            if (ad.link.isNullOrEmpty()) {
                Glide.with(itemView.context)
                    .load(R.drawable.ic_katalog_vozila)
                    .into(adPicture)
            } else {
                Glide.with(itemView.context)
                    .load(ad.link) // URL slike
                    .placeholder(R.drawable.ic_katalog_vozila)
                    .error(R.drawable.ic_katalog_vozila)
                    .into(adPicture)
            }

            itemView.setOnClickListener {
                onAdClick(ad)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsAdapter.AdViewHolder {
        val adView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.ad_list_item, parent, false)
        return AdViewHolder(adView)
    }

    override fun getItemCount(): Int {
        return adsData.size
    }

    override fun onBindViewHolder(holder: AdViewHolder, position: Int) {
        holder.bind(adsData[position])
    }

    fun updateItems(list: List<Ad>){
        adsData = list
        notifyDataSetChanged()
    }
}