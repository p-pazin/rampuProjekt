package com.example.carchive.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.carchive.databinding.FragmentAddCarBinding
import com.example.carchive.adapters.InfoPhotosPagerAdapter
import com.example.carchive.viewmodels.VehicleCatalogViewModel
import com.google.android.material.tabs.TabLayoutMediator

class AddVehicleFragment : Fragment() {

    private var _binding: FragmentAddCarBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCarBinding.inflate(inflater, container, false)

        binding.navBackButton.backButton.setOnClickListener(){
            findNavController().popBackStack()
        }

        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout
        val vmVehicle: VehicleCatalogViewModel by activityViewModels()
        viewPager.adapter = InfoPhotosPagerAdapter(this)

        viewPager.isUserInputEnabled = false

        vmVehicle.isBasicInfoComplete.observe(viewLifecycleOwner) { isComplete ->
            viewPager.isUserInputEnabled = isComplete

            val secondTab = tabLayout.getTabAt(1)
            secondTab?.view?.isEnabled = isComplete
        }

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Opće Informacije"
                1 -> "Dodaj Slike"
                else -> null
            }
        }.attach()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
