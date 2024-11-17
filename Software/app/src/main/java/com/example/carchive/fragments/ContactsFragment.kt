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
import com.example.carchive.CarchiveActivity
import com.example.carchive.R
import com.example.carchive.adapters.ContactsAdapter
import com.example.carchive.databinding.ActivityCarchiveBinding
import com.example.carchive.databinding.FragmentCarCatalogBinding
import com.example.carchive.databinding.FragmentContactsBinding
import com.example.carchive.helpers.MockDataLoader

class ContactsFragment : Fragment() {

    private val contactsData = MockDataLoader.getMockContacts()
    private lateinit var recyclerView: RecyclerView
    private lateinit var toggleButton: ImageButton
    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rv_contact_list)
        recyclerView.adapter = ContactsAdapter(MockDataLoader.getMockContacts())
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        binding.sidebarLogo.drawerToggleButton.setOnClickListener(){
            (activity as? CarchiveActivity)?.toggleDrawer()
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as? CarchiveActivity)?.setDrawerEnabled(true)
    }

    override fun onPause() {
        super.onPause()
        (activity as? CarchiveActivity)?.setDrawerEnabled(false)
    }

}