package com.example.carchive.fragments

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
import com.example.carchive.adapters.AdsAdapter
import com.example.carchive.databinding.FragmentAdsBinding
import com.example.carchive.entities.Ad
import com.example.carchive.viewmodels.AdViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AdFragment : Fragment() {
    private var _binding: FragmentAdsBinding? = null
    private val binding get() = _binding!!
    private val adViewModel: AdViewModel by viewModels()
    private lateinit var adsAdapter: AdsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.sidebarLogo.drawerToggleButton.setOnClickListener {
            (activity as? CarchiveActivity)?.toggleDrawer()
        }

        setupRecyclerView()
        observeAds()
        adViewModel.fetchAds()
    }

    private fun setupRecyclerView() {
        adsAdapter = AdsAdapter(listOf()) { ad : Ad ->
            val bundle = Bundle().apply {
                putSerializable("ad_key", ad)
            }
        }

        binding.recyclerViewAds.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = adsAdapter
        }
    }

    private fun observeAds() {
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                adViewModel.ads.collectLatest { ads ->
                    if (ads.isEmpty()) {
                        binding.tvNoAds.visibility = View.VISIBLE
                        binding.recyclerViewAds.visibility = View.GONE
                    } else {
                        binding.tvNoAds.visibility = View.GONE
                        binding.recyclerViewAds.visibility = View.VISIBLE
                        adsAdapter.updateItems(ads)
                    }
                }
            }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
