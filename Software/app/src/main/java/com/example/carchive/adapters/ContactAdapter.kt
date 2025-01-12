package com.example.carchive.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.carchive.R
import com.example.carchive.entities.Contact
import com.example.carchive.entities.Vehicle

class ContactsAdapter(
    var contactsData : List<Contact>,
    private val onContactClick:(Contact) -> Unit) : RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {

    inner class ContactViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        private val contactName: TextView
        private val ID: TextView
        private val contactEmail: TextView
        private val contactPhone: TextView
        private val contactPin: TextView
        private val contactActivity: TextView
        private val contactState: TextView
        private val contactStateIcon : ImageView
        private val contactOfferSent: TextView
        private val contactOfferSentIcon : ImageView

        init {
            contactName = view.findViewById(R.id.tv_contact_name)
            ID = view.findViewById(R.id.tv_contact_id)
            contactEmail = view.findViewById(R.id.tv_contact_email)
            contactPhone = view.findViewById(R.id.tv_contact_phone)
            contactPin = view.findViewById(R.id.tv_contact_pin)
            contactActivity = view.findViewById(R.id.tv_contact_activity)
            contactState = view.findViewById(R.id.tv_contact_state)
            contactStateIcon = view.findViewById(R.id.iv_contact_state)
            contactOfferSent = view.findViewById(R.id.tv_contact_offer_sent)
            contactOfferSentIcon = view.findViewById(R.id.iv_contact_offer_sent)
        }
        fun bind(contact : Contact) {
            contactName.text = contact.firstName + " " + contact.lastName
            ID.text = "ID: " + contact.id
            contactEmail.text = contact.email
            contactPhone.text = contact.mobileNumber
            contactPin.text = contact.pin
            contactState.text = contact.state.toString()
            contactActivity.text = "Activity"
            if(contact.state == 1) {
                contactStateIcon.setImageResource(R.drawable.ic_aktivan_kontakt)
            }
            else {
                contactStateIcon.setImageResource(R.drawable.ic_neaktivan_kontakt)
            }
            if(true) {
                contactOfferSent.text = "Ponuda poslana"
                contactOfferSentIcon.setImageResource(R.drawable.ic_check)
            }
            else {
                contactOfferSent.text = "Ponuda nije poslana"
                contactOfferSentIcon.setImageResource(R.drawable.ic_x)
            }
            itemView.setOnClickListener(){
                onContactClick(contact)
            }
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

    fun addContact(newContact: Contact) {
        val position = contactsData.size
        //contactsData.add(newContact)
        notifyItemInserted(position)
    }
    fun updateContact(updatedContact: Contact, position: Int) {
        if (position >= 0 && position < contactsData.size) {
            //contactsData[position] = updatedContact
            notifyItemChanged(position)
        }
    }
    fun removeContact(contact: Contact) {
        val position = contactsData.indexOf(contact)
        if(position >= 0) {
            //contactsData.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun updateItems(list: List<Contact>){
        contactsData = list
        notifyDataSetChanged()
    }
}