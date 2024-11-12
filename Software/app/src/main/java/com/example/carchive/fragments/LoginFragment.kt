package com.example.carchive.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.carchive.MainActivity
import com.example.carchive.R
import com.example.carchive.databinding.FragmentLoginBinding
import com.example.carchive.helpers.MockDataLoader


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout using ViewBinding
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val binding = _binding!!

        // Access views using binding
        val emailEditText = binding.etEmail
        val passwordEditText = binding.etPassword
        val loginButton = binding.btnLogin
        val registerButton = binding.btnRegister

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Find user in mock data with matching email and password
            val user = MockDataLoader.getUsers().find {
                it.emailAddress == email && it.password == password
            }

            if (user != null) {
                // Navigate to main activity if login is successful
                Toast.makeText(requireContext(), "Prijava uspješna!", Toast.LENGTH_SHORT).show()
                (activity as MainActivity).navigateToCarchive()
            } else {
                Toast.makeText(requireContext(), "Neispravan email ili lozinka", Toast.LENGTH_SHORT).show()
            }
        }

        registerButton.setOnClickListener {
            // Navigate to RegisterFragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, RegisterFragment())
                .addToBackStack(null)
                .commit()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Set binding to null to prevent memory leaks
        _binding = null
    }




}
