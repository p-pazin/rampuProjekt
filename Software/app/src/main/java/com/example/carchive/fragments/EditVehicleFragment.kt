package com.example.carchive.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.carchive.adapters.EditInfoPagerAdapter
import com.example.carchive.databinding.FragmentAddCarBinding
import com.example.carchive.databinding.FragmentEditCarBinding
import com.google.android.material.tabs.TabLayoutMediator

class EditVehicleFragment : Fragment() {

    private var _binding: FragmentEditCarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditCarBinding.inflate(inflater, container, false)

        binding.navBackButton.backButton.setOnClickListener(){
            findNavController().popBackStack()
        }

        val viewPager = binding.editViewPager
        val tabLayout = binding.editTabLayout
        val adapter = EditInfoPagerAdapter(this, arguments ?: Bundle())
        viewPager.adapter = adapter

        val args = arguments
        val urediOpceInformacijeFragment = EditBasicInfoFragment()
        urediOpceInformacijeFragment.arguments = args

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

