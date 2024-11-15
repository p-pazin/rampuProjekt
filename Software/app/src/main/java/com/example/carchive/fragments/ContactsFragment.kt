package com.example.carchive.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carchive.R
import com.example.carchive.adapters.ContactsAdapter
import com.example.carchive.helpers.MockDataLoader

class ContactsFragment : Fragment() {

    private val contactsData = MockDataLoader.getMockContacts()
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contacts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.rv_contact_list)
        recyclerView.adapter = ContactsAdapter(MockDataLoader.getMockContacts())
        recyclerView.layoutManager = LinearLayoutManager(view.context)
    }

}