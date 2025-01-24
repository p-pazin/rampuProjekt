package com.example.carchive.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.carchive.R
import com.example.carchive.data.dto.ReservationDetails

class ReservationAdapter(
    private var reservations: List<ReservationDetails>,
    private val onReservationClick: (Int) -> Unit
) : RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder>() {

    inner class ReservationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvReservationId: TextView = itemView.findViewById(R.id.tv_reservation_id)
        private val tvContact: TextView = itemView.findViewById(R.id.tv_reservation_contact)
        private val tvVehicle: TextView = itemView.findViewById(R.id.tv_reservation_vehicle)
        private val tvMaxMileage: TextView = itemView.findViewById(R.id.tv_reservation_maxMileage)
        private val tvPrice: TextView = itemView.findViewById(R.id.tv_reservation_price)
        private val tvStartDate: TextView = itemView.findViewById(R.id.tv_reservation_startDate)
        private val tvEndDate: TextView = itemView.findViewById(R.id.tv_reservation_endDate)

        fun bind(reservationDetails: ReservationDetails) {
            tvReservationId.text = "ID: ${reservationDetails.reservation.id}"
            tvContact.text = reservationDetails.contact.firstName + " " + reservationDetails.contact.lastName
            tvVehicle.text = reservationDetails.vehicle.model + " " + reservationDetails.vehicle.brand
            tvMaxMileage.text = "Kilometara: ${reservationDetails.reservation.maxMileage}"
            tvPrice.text = "Cijena: ${reservationDetails.reservation.price} €"
            tvStartDate.text = "Od: ${reservationDetails.reservation.startDate}"
            tvEndDate.text = "Do: ${reservationDetails.reservation.endDate}"

            itemView.setOnClickListener {
                onReservationClick(reservationDetails.reservation.id)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.reservation_list_item, parent, false)
        return ReservationViewHolder(view)
    }

    override fun getItemCount(): Int = reservations.size

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        holder.bind(reservations[position])
    }

    fun updateReservations(newReservations: List<ReservationDetails>) {
        reservations = newReservations
        notifyDataSetChanged()
    }
}
