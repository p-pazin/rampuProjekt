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
import com.example.carchive.databinding.FragmentLoginBinding
import com.example.carchive.services.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val binding = _binding!!

        val emailEditText = binding.etEmail
        val passwordEditText = binding.etPassword
        val loginButton = binding.btnLogin
        val registerButton = binding.btnRegister

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()


            val networkClient = Network().getInstance()
            val tokenManager = TokenManager()

            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val token = async {
                        networkClient.login(LoginRequestDto(email, password))
                    }.await().token
                    tokenManager.saveToken(token)

                    withContext(Dispatchers.Main){
                        Toast.makeText(requireContext(), "Prijava uspješna!", Toast.LENGTH_SHORT).show()
                        startActivity(CarchiveActivity.newInstance(requireContext()))
                        requireActivity().finish()
                    }
                }
                catch (exception: Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(requireContext(), "Doslo je do pogreske, probajte ponovno!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}
