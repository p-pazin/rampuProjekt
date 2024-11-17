package com.example.carchive.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.carchive.fragments.AddPicturesFragment
import com.example.carchive.fragments.EditBasicInfoFragment

class EditInfoPagerAdapter(fragment: Fragment, private val args: Bundle) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> EditBasicInfoFragment().apply {
                arguments = args
            }
            else -> AddPicturesFragment()
        }
    }
}