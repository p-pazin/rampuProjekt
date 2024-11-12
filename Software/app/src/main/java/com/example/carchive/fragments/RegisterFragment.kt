package com.example.carchive.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.carchive.databinding.FragmentRegisterBinding
import com.example.carchive.entities.User
import com.example.carchive.helpers.MockDataLoader

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
            val name = binding.etName.text.toString()
            val lastName = binding.etSurname.text.toString()
            val email = binding.etRegisterEmail.text.toString()
            val phone = binding.etPhoneNumber.text.toString()
            val password = binding.etRegisterPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            if (name.isNotEmpty() && lastName.isNotEmpty() && email.isNotEmpty() &&
                phone.isNotEmpty() && password.isNotEmpty()
            ) {
                if(password == confirmPassword)
                {
                    val newUser = User(
                        id = MockDataLoader.getLastID() + 1,
                        firstName = name,
                        lastName = lastName,
                        phoneNumber = phone,
                        emailAddress = email,
                        password = password
                    )

                    print(newUser)
                    Log.d("TAG", newUser.toString())

                    MockDataLoader.addUser(newUser)
                    Toast.makeText(requireContext(), "Uspjesno registirani!", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
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
