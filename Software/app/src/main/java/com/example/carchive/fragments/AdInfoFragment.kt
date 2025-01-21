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
import com.example.carchive.R
import com.example.carchive.adapters.AdsAdapter
import com.example.carchive.adapters.ImagePagerAdapter
import com.example.carchive.databinding.FragmentAdDetailsBinding
import com.example.carchive.viewmodels.AdViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdInfoFragment : Fragment() {
    private var _binding: FragmentAdDetailsBinding? = null
    private val binding get() = _binding!!
    private val adDetailsViewModel: AdViewModel by activityViewModels()
    private lateinit var pagerAdapter: ImagePagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.navBackButton.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            adDetailsViewModel.ad.collect { ad ->
                val adapter = ImagePagerAdapter(ad.links)
                binding.vpAdImages.adapter = adapter
                binding.tvAdTitle.text = ad.title
                binding.tvAdId.text = "ID: ${ad.id}"
                binding.tvAdDescription.text = "Opis: ${ad.description}"
                binding.tvAdPaymentMethod.text = "Način plaćanja: ${ad.paymentMethod}"
                binding.tvAdDate.text = "Datum objave: ${ad.dateOfPublishment}"
                binding.tvAdVehicle.text = "Vozilo: ${ad.brand} ${ad.model}"

                binding.btnAdDetailsEdit.setOnClickListener {
                    val bundle = Bundle().apply {
                        putInt("adId", ad.id)
                        putString("adTitle", ad.title)
                        putString("adDescription", ad.description)
                        putString("adPaymentMethod", ad.paymentMethod)
                        putString("adBrand", ad.brand)
                        putString("adModel", ad.model)
                        putString("adDate", ad.dateOfPublishment)
                    }
                    findNavController().navigate(R.id.action_adInfoFragment_to_adEditFragment, bundle)
                }
                binding.btnAdDetailsDelete.setOnClickListener {
                    adDetailsViewModel.deleteAd(ad.id)
                    findNavController().popBackStack()
                }
            }
        }
    }

}
