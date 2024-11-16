package com.example.carchive.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.carchive.adapters.EditInfoPagerAdapter
import com.example.carchive.databinding.FragmentAddCarBinding
import com.google.android.material.tabs.TabLayoutMediator

class EditCarFragment : Fragment() {

    private var _binding: FragmentAddCarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCarBinding.inflate(inflater, container, false)

        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout
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

