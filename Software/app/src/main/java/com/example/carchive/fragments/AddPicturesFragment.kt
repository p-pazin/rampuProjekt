package com.example.carchive.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.carchive.R
import com.example.carchive.adapters.PicturesAdapter
import androidx.recyclerview.widget.GridLayoutManager

class AddPicturesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_pictures, container, false)

        // List of drawable resources to display in the RecyclerView
        val pictures = listOf(
            R.drawable.ic_katalog_vozila,
            R.drawable.ic_katalog_vozila,
            R.drawable.ic_katalog_vozila // Add your drawable resources here
        )

        // Initialize RecyclerView and set up the adapter with GridLayoutManager
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 3) // 3 is the number of columns
        recyclerView.adapter = PicturesAdapter(pictures)

        return view
    }
}
