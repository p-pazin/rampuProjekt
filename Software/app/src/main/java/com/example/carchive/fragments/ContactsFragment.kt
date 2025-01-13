package com.example.carchive.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carchive.CarchiveActivity
import com.example.carchive.R
import com.example.carchive.adapters.ContactsAdapter
import com.example.carchive.databinding.ActivityCarchiveBinding
import com.example.carchive.databinding.FragmentCarCatalogBinding
import com.example.carchive.databinding.FragmentContactsBinding
import com.example.carchive.entities.Contact
import com.example.carchive.helpers.MockDataLoader
import kotlinx.coroutines.launch

class ContactsFragment : Fragment() {

    private val viewModel : ContactsViewModel by viewModels()
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

        val btnDodaj = binding.sidebarLogo.btnDodaj
        btnDodaj.setOnClickListener {
            findNavController().navigate(R.id.action_contactsFragment_to_contactAddFragment)
        }

        recyclerView = binding.rvContactList
        val adapter = ContactsAdapter(emptyList()) { contact: Contact ->
            val bundle = Bundle().apply {
                putSerializable("contact_key", contact)
            }
            findNavController().navigate(R.id.action_contactsFragment_to_contactsDetailsFragment, bundle)
        }
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = adapter

        binding.sidebarLogo.drawerToggleButton.setOnClickListener {
            (activity as? CarchiveActivity)?.toggleDrawer()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.contacts.collect { contacts ->
                adapter.updateItems(contacts)
            }
        }

        viewModel.fetchContacts()
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