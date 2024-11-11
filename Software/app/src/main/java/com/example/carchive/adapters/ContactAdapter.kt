package com.example.carchive.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.carchive.R
import com.example.carchive.entities.Contact

class ContactsAdapter(val contactsData : List<Contact>) : RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {
    inner class ContactViewHolder(view : View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val contactView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.contact_list_item, parent, false)
        return ContactViewHolder(contactView)
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}