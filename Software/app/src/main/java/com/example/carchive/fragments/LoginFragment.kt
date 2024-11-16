package com.example.carchive.fragments

import CarAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.carchive.CarchiveActivity
import com.example.carchive.MainActivity
import com.example.carchive.R
import com.example.carchive.databinding.FragmentLoginBinding
import com.example.carchive.entities.Car
import com.example.carchive.helpers.MockDataLoader
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


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

            val user = MockDataLoader.getUsers().find {
                it.emailAddress == email && it.password == password
            }
            val intent = Intent(requireContext(), CarchiveActivity::class.java)
            if (user != null) {
                Toast.makeText(requireContext(), "Prijava uspješna!", Toast.LENGTH_SHORT).show()
                startActivity(CarchiveActivity.newInstance(requireContext()))
                requireActivity().finish()

            } else {
                Toast.makeText(requireContext(), "Neispravan email ili lozinka", Toast.LENGTH_SHORT).show()
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
