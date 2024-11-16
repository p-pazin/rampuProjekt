package com.example.carchive.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.carchive.fragments.DodajSlikeFragment
import com.example.carchive.fragments.OpceInformacijeFragment

class InfoPhotosPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OpceInformacijeFragment()
            else -> DodajSlikeFragment()
        }
    }
}
