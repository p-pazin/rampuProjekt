package com.example.carchive.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.carchive.R
import com.example.carchive.entities.Contact

class ContactsAdapter(val contactsData : List<Contact>) : RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {
    inner class ContactViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        private val contactName: TextView
        private val ID: TextView
        private val contactEmail: TextView
        private val contactPhone: TextView
        private val contactPin: TextView
        private val contactState: TextView
        private val contactActivity: TextView
        private val contactOfferState: TextView
        private val contactActivityIcon : ImageView
        private val contactOfferStateIcon : ImageView

        init {
            contactName = view.findViewById(R.id.txt_contact_name)
            ID = view.findViewById(R.id.txt_contact_id)
            contactEmail = view.findViewById(R.id.txt_contact_email)
            contactPhone = view.findViewById(R.id.txt_contact_phone)
            contactPin = view.findViewById(R.id.txt_contact_pin)
            contactState = view.findViewById(R.id.txt_contact_state)
            contactActivity = view.findViewById(R.id.txt_contact_activity)
            contactOfferState = view.findViewById(R.id.txt_contact_offer_state)
            contactActivityIcon = view.findViewById(R.id.img_contact_activity)
            contactOfferStateIcon = view.findViewById(R.id.img_contact_offer_state)
        }
        fun bind(contact : Contact) {
            contactName.text = contact.firstName + " " + contact.lastName
            ID.text = "ID: " + contact.id
            contactEmail.text = contact.emailAddress
            contactPhone.text = contact.mobilePhoneNumber
            contactPin.text = contact.pin
            contactState.text = contact.state
            contactActivity.text = contact.activity.name
            contactActivityIcon.setImageResource(contact.activity.icon)
            contactOfferState.text = contact.offerState.name
            contactOfferStateIcon.setImageResource(contact.offerState.icon)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val contactView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.contact_list_item, parent, false)
        return ContactViewHolder(contactView)
    }

    override fun getItemCount(): Int {
        return contactsData.size
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(contactsData[position])
    }
}