// Create a new file: FragmentPagerAdapter.kt
package com.example.carchive.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.carchive.fragments.DodajSlikeFragment
import com.example.carchive.fragments.OpceInformacijeFragment

class InfoPhotosPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2  // We have two tabs

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OpceInformacijeFragment()  // First tab
            else -> DodajSlikeFragment()
        }
    }
}
