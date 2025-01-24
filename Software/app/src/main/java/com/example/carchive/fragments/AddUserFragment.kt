package com.example.carchive.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.carchive.data.dto.NewUserDto
import com.example.carchive.data.network.Result
import com.example.carchive.databinding.FragmentAddUserBinding
import com.example.carchive.viewmodels.CompanyUserViewModel

class AddUserFragment : Fragment() {
    private var _binding: FragmentAddUserBinding? = null
    private val binding get() = _binding!!
    private val companyUserViewModel: CompanyUserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.navBackButton.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnUserAdd.setOnClickListener {
            val firstName = binding.etUserFirstname.text.toString()
            val lastName = binding.etUserLastname.text.toString()
            val email = binding.etUserEmail.text.toString()
            val password = binding.etUserPassword.text.toString()

            if (firstName.isNotBlank() && lastName.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                val newUserDto = NewUserDto(firstName, lastName, email, password)
                companyUserViewModel.addNewUser(newUserDto)
                companyUserViewModel.newUserResult.observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is Result.Success -> {
                            Toast.makeText(requireContext(), "Uspješno dodana osoba", Toast.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                        }
                        is Result.Error -> {
                            Toast.makeText(requireContext(), "Neuspješno dodana osoba", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            } else {
                Toast.makeText(requireContext(), "Svi podaci moraju biti popunjeni", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
