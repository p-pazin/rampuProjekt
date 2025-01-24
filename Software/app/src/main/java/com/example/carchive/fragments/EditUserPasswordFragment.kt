package com.example.carchive.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.carchive.data.dto.NewPasswordDto
import com.example.carchive.data.dto.UserDto
import com.example.carchive.data.network.Result
import com.example.carchive.databinding.FragmentEditUserPasswordBinding
import com.example.carchive.viewmodels.CompanyUserViewModel
import kotlinx.coroutines.launch

class EditUserPasswordFragment : Fragment() {
    private var _binding: FragmentEditUserPasswordBinding? = null
    private val binding get() = _binding!!
    private val companyUserViewModel: CompanyUserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditUserPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.navBackButton.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        val userEmail = arguments?.getString("userEmail") ?: ""

        if (userEmail.isNotEmpty()) {
            binding.etUserEmail.setText(userEmail)
        } else {
            Toast.makeText(context, "Korisnički email nije pronađen.", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }


        companyUserViewModel.newUserPasswordResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    Toast.makeText(context, "Lozinka uspješno promijenjena!", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
                is Result.Error -> {
                    Toast.makeText(
                        context,
                        "Greška prilikom promjene lozinke: ${result.error.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    Toast.makeText(context, "Došlo je do neočekivane greške.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnUserEditPassword.setOnClickListener {
            val currentPassword = binding.etUserPassword.text.toString()
            val newPassword = binding.etUserNewpassword.text.toString()
            val userEmail = binding.etUserEmail.text.toString()

            if (currentPassword.isEmpty()) {
                Toast.makeText(context, "Molimo unesite trenutnu lozinku.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPassword.isEmpty()) {
                Toast.makeText(context, "Molimo unesite novu lozinku.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (userEmail.isEmpty()) {
                Toast.makeText(context, "Korisnički email nije pronađen.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newPasswordDto = NewPasswordDto(
                email = userEmail,
                password = currentPassword,
                newPassword = newPassword
            )
            companyUserViewModel.changeUserPassword(newPasswordDto)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

