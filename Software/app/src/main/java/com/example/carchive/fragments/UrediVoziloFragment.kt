package com.example.carchive.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.carchive.adapters.UrediInfoPagerAdapter
import com.example.carchive.databinding.DodajVoziloBinding
import com.google.android.material.tabs.TabLayoutMediator

class UrediVoziloFragment : Fragment() {

    private var _binding: DodajVoziloBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DodajVoziloBinding.inflate(inflater, container, false)

        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout
        val adapter = UrediInfoPagerAdapter(this, arguments ?: Bundle())
        viewPager.adapter = adapter

        val args = arguments
        val urediOpceInformacijeFragment = UrediOpceInformacijeFragment()
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

