package com.example.carchive.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.carchive.R
import com.example.carchive.data.dto.OfferDto

class OfferAdapter(
    private var offers: List<OfferDto>,
    private val onOfferClick: (OfferDto) -> Unit
) : RecyclerView.Adapter<OfferAdapter.OfferViewHolder>() {

    inner class OfferViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        private val tvDateOfCreation: TextView = itemView.findViewById(R.id.tvDateOfCreation)
        private val cardOffer: CardView = itemView.findViewById(R.id.cardOffer)

        fun bind(offer: OfferDto) {
            tvTitle.text = offer.title
            tvPrice.text = "Cijena: ${offer.price} €"
            tvDateOfCreation.text = "Stvoreno datuma: ${offer.dateOfCreation}"

            cardOffer.setOnClickListener {
                onOfferClick(offer)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.offer_item, parent, false)
        return OfferViewHolder(view)
    }

    override fun getItemCount(): Int = offers.size

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        holder.bind(offers[position])
    }

    fun updateOffers(newOffers: List<OfferDto>) {
        offers = newOffers
        notifyDataSetChanged()
    }
}

