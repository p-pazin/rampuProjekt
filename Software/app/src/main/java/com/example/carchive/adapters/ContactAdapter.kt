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
        private val contactCity: TextView
        private val contactCountry: TextView
        private val contactMobile: TextView
        private val contactState: TextView
        private val contactStateIcon : ImageView

        init {
            contactName = view.findViewById(R.id.tv_contact_name)
            ID = view.findViewById(R.id.tv_contact_id)
            contactEmail = view.findViewById(R.id.tv_contact_email)
            contactCity = view.findViewById(R.id.tv_contact_city)
            contactCountry = view.findViewById(R.id.tv_contact_country)
            contactMobile = view.findViewById(R.id.tv_contact_mobile_number)
            contactState = view.findViewById(R.id.tv_contact_state)
            contactStateIcon = view.findViewById(R.id.iv_contact_state)
        }
        fun bind(contact : Contact) {
            contactName.text = contact.firstName + " " + contact.lastName
            ID.text = "ID: " + contact.id
            contactEmail.text = contact.email
            contactCity.text = contact.city
            contactCountry.text = contact.country
            contactMobile.text = contact.mobileNumber
            if(contact.state == 1) {
                contactStateIcon.setImageResource(R.drawable.ic_aktivan_kontakt)
                contactState.text = "Aktivan"
            }
            else {
                contactStateIcon.setImageResource(R.drawable.ic_neaktivan_kontakt)
                contactState.text = "Neaktivan"
            }
            itemView.setOnClickListener(){
                onContactClick(contact)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsAdapter.ContactViewHolder {
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