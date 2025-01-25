package com.example.carchive.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carchive.CarchiveActivity
import com.example.carchive.R
import com.example.carchive.adapters.InvoiceAdapter
import com.example.carchive.adapters.OfferAdapter
import com.example.carchive.databinding.FragmentContactsBinding
import com.example.carchive.databinding.FragmentInvoiceCatalogBinding
import com.example.carchive.viewmodels.InvoiceViewModel
import com.example.carchive.viewmodels.VehicleCatalogViewModel
import kotlinx.coroutines.launch


class InvoiceCatalogFragment: Fragment() {

    private lateinit var recyclerView: RecyclerView
    private var _binding: FragmentInvoiceCatalogBinding? = null
    private val binding get() = _binding!!

    private lateinit var invoiceAdapter: InvoiceAdapter

    private val vmInvoices: InvoiceViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInvoiceCatalogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        invoiceAdapter = InvoiceAdapter(emptyList())
        recyclerView = binding.recyclerViewInvoices

        binding.sidebarLogo.drawerToggleButton.setOnClickListener {
            (activity as? CarchiveActivity)?.toggleDrawer()
        }

        binding.sidebarLogo.btnDodaj.setOnClickListener{
            findNavController().navigate(R.id.action_invoicesFragment_to_addInvoiceFragment)
        }

        setupRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch {
            vmInvoices.invoices.collect { data ->
                invoiceAdapter.updateItems(data)
            }
        }

        vmInvoices.fetchInvoices()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewInvoices.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = invoiceAdapter
        }
    }
}