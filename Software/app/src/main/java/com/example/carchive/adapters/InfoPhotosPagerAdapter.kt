package com.example.carchive.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.carchive.fragments.AddPicturesFragment
import com.example.carchive.fragments.BasicInfoFragment

class InfoPhotosPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BasicInfoFragment()
            else -> AddPicturesFragment()
        }
    }
}
