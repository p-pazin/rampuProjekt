package com.example.carchive.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.carchive.data.dto.NewCompanyDto
import com.example.carchive.data.repositories.UserRepository
import com.example.carchive.databinding.FragmentRegisterBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            val name = binding.etCompanyName.text.toString()
            val city = binding.etCompanyCity.text.toString()
            val address = binding.etCompanyAddress.text.toString()
            val pin = binding.etCompanyPin.text.toString()
            val firstname = binding.etName.text.toString()
            val lastName = binding.etSurname.text.toString()
            val email = binding.etRegisterEmail.text.toString()
            val password = binding.etRegisterPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            if (name.isNotEmpty() && city.isNotEmpty() && address.isNotEmpty() && pin.isNotEmpty() && firstname.isNotEmpty()
                && lastName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()
            ) {
                if(password == confirmPassword)
                {

                    val authRepository = UserRepository()

                    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                        try {
                            async {
                                authRepository.registerCompany(NewCompanyDto(name,city,address,pin,firstname, lastName, email, password))
                            }.await()
                            withContext(Dispatchers.Main){
                                Toast.makeText(requireContext(), "Uspjesno registirani!", Toast.LENGTH_SHORT).show()
                                findNavController().popBackStack()
                            }

                        }
                        catch (exception: Exception){
                            withContext(Dispatchers.Main){
                                Toast.makeText(requireContext(), "Doslo je do pogreske, probajte ponovno!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                } else{
                    Toast.makeText(requireContext(), "Lozinke trebaju biti jednake", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(requireContext(), "Popunite sve", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
