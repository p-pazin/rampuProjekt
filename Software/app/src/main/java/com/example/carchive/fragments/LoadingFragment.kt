package com.example.carchive.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.carchive.CarchiveActivity
import com.example.carchive.R
import com.example.carchive.data.dto.LoginRequestDto
import com.example.carchive.data.network.Network
import com.example.carchive.data.network.Result
import com.example.carchive.databinding.FragmentLoadingBinding
import com.example.carchive.services.TokenManager
import com.example.carchive.util.safeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoadingFragment : Fragment() {
    private var _binding: FragmentLoadingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoadingBinding.inflate(inflater, container, false)
        val binding = _binding!!


        val networkClient = Network().getInstance()

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val result = async {
                safeResponse {
                    networkClient.getUser()
                }
            }.await()
            withContext(Dispatchers.Main) {
                when (result) {
                    is Result.Error -> findNavController().navigate(R.id.action_loadingFragment_to_loginFragment)
                    is Result.Success -> {
                        startActivity(CarchiveActivity.newInstance(requireContext()))
                        requireActivity().finish()
                    }
                }
            }
        }
        return binding.root
    }
}