package com.example.carchive.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.carchive.databinding.FragmentEditUserBinding
import com.example.carchive.viewmodels.CompanyUserViewModel
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.carchive.data.dto.UserDto
import com.example.carchive.data.network.Result
import kotlinx.coroutines.launch

class EditUserInfoFragment : Fragment() {
    private var _binding: FragmentEditUserBinding? = null
    private val binding get() = _binding!!
    private val companyUserViewModel: CompanyUserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            companyUserViewModel.user.collect { user ->
                if (user != UserDto.EMPTY) {
                    binding.etUserFirstname.setText(user.firstName)
                    binding.etUserLastname.setText(user.lastName)
                    binding.etUserEmail.setText(user.email)
                }
            }
        }

        binding.btnUserEdit.setOnClickListener {
            val firstName = binding.etUserFirstname.text.toString().trim()
            val lastName = binding.etUserLastname.text.toString().trim()
            val email = binding.etUserEmail.text.toString().trim()

            if (firstName.isNotBlank() && lastName.isNotBlank() && email.isNotBlank() &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                val updatedUser = UserDto(
                    id = companyUserViewModel.user.value.id,
                    firstName = firstName,
                    lastName = lastName,
                    email = email
                )
                companyUserViewModel.updateUser(updatedUser)
                companyUserViewModel.newUserResult.observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is Result.Success -> {
                            Toast.makeText(requireContext(), "Osoba uspješno ažurirana", Toast.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                            companyUserViewModel.fetchCompanyUsers()
                        }
                        is Result.Error -> {
                            Toast.makeText(requireContext(), "Greška pri ažuriranju osobe", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Ispravno popunite sva polja", Toast.LENGTH_SHORT).show()
            }
        }

        binding.navBackButton.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
