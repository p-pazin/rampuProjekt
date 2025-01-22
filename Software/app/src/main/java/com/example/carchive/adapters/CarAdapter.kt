package com.example.carchive.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.carchive.R
import com.example.carchive.entities.Vehicle

class CarAdapter(
    private var vehicles: List<Vehicle>,
    private val onDetailsClick: (View, Vehicle) -> Unit,
    private val onCarCardClick: (View, Vehicle) -> Unit,
    private val loadImage: (Int, (String?) -> Unit) -> Unit
) : RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    private val imageCache = mutableMapOf<Int, String?>()

    inner class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val carCard: LinearLayout = itemView.findViewById(R.id.carCard)
        private val imageCar: ImageView = itemView.findViewById(R.id.imageCar)
        private val textModel: TextView = itemView.findViewById(R.id.textModel)
        private val textRegistration: TextView = itemView.findViewById(R.id.textRegistration)
        private val textKilometers: TextView = itemView.findViewById(R.id.textKilometers)
        private val textSellsRents: TextView = itemView.findViewById(R.id.textSellsRents)
        private val textPrice: TextView = itemView.findViewById(R.id.textPrice)
        private val imgBtnDetails: ImageView = itemView.findViewById(R.id.imgBtnDetails)

        @SuppressLint("SetTextI18n")
        fun bind(vehicle: Vehicle) {
            textModel.text = "${vehicle.brand} ${vehicle.model}"
            textModel.text = "${vehicle.brand} ${vehicle.model}"
            textRegistration.text = vehicle.registration
            textKilometers.text = "${vehicle.kilometers} km"
            textSellsRents.text = if (vehicle.rentSell) "U prodaji" else "U najmu"
            textPrice.text = "$${vehicle.price}"

            if (imageCache.containsKey(vehicle.id)) {
                loadImageIntoView(imageCache[vehicle.id], imageCar)
            } else {
                loadImage(vehicle.id) { imageUrl ->
                    imageCache[vehicle.id] = imageUrl
                    loadImageIntoView(imageUrl, imageCar)
                }
            }

            imgBtnDetails.setOnClickListener { view ->
                onDetailsClick(view, vehicle)
            }

            carCard.setOnClickListener { view ->
                onCarCardClick(view, vehicle)
            }
        }

        private fun loadImageIntoView(imageUrl: String?, imageView: ImageView) {
            Glide.with(itemView.context)
                .load(imageUrl ?: R.drawable.ic_katalog_vozila)
                .placeholder(R.drawable.ic_katalog_vozila)
                .error(R.drawable.ic_x)
                .into(imageView)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_car, parent, false)
        return CarViewHolder(view)
    }

    override fun getItemCount(): Int = vehicles.size

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        holder.bind(vehicles[position])
    }

    fun updateItems(newVehicles: List<Vehicle>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize() = vehicles.size
            override fun getNewListSize() = newVehicles.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return vehicles[oldItemPosition].id == newVehicles[newItemPosition].id
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return vehicles[oldItemPosition] == newVehicles[newItemPosition]
            }
        }

        val diffResult = DiffUtil.calculateDiff(diffCallback)
        vehicles = newVehicles
        diffResult.dispatchUpdatesTo(this)
    }
}

