package com.example.carchive.fragments

import com.example.carchive.viewmodels.OfferViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carchive.CarchiveActivity
import com.example.carchive.R
import com.example.carchive.adapters.OfferAdapter
import com.example.carchive.data.dto.OfferDto
import com.example.carchive.databinding.FragmentOffersCatalogBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class OffersFragment : Fragment() {
    private var _binding: FragmentOffersCatalogBinding? = null
    private val binding get() = _binding!!
    private val offerViewModel: OfferViewModel by viewModels()
    private lateinit var offerAdapter: OfferAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOffersCatalogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sidebarLogo.drawerToggleButton.setOnClickListener {
            (activity as? CarchiveActivity)?.toggleDrawer()
        }

        setupRecyclerView()
        observeOffers()
        offerViewModel.fetchOffers()
    }

    private fun setupRecyclerView() {
        offerAdapter = OfferAdapter(listOf()) { offer ->
            navigateToOfferDetails(offer)
        }
        binding.recyclerViewOffers.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = offerAdapter
        }
    }

    private fun observeOffers() {
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                offerViewModel.offers.collectLatest { offers ->
                    if (offers.isEmpty()) {
                        binding.tvNoOffers.visibility = View.VISIBLE
                        binding.recyclerViewOffers.visibility = View.GONE
                    } else {
                        binding.tvNoOffers.visibility = View.GONE
                        binding.recyclerViewOffers.visibility = View.VISIBLE
                        offerAdapter.updateOffers(offers)
                    }
                }
            }
        }
    }

    private fun navigateToOfferDetails(offer: OfferDto) {
        val bundle = Bundle().apply {
            putInt("id", offer.id)
            putString("title", offer.title)
            putDouble("price", offer.price)
            putString("dateOfCreation", offer.dateOfCreation)
        }
        findNavController().navigate(R.id.action_offersFragment_to_offerDetailsFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

