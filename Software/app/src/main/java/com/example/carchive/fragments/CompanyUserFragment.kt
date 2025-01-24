package com.example.carchive.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.carchive.CarchiveActivity
import com.example.carchive.R
import com.example.carchive.data.dto.CompanyDto
import com.example.carchive.data.dto.UserDto
import com.example.carchive.databinding.FragmentCompanyAndUserInfoBinding
import com.example.carchive.services.TokenManager
import com.example.carchive.viewmodels.CompanyUserViewModel
import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.DecodedJWT
import kotlinx.coroutines.launch

class CompanyUserFragment : Fragment() {
    private var _binding: FragmentCompanyAndUserInfoBinding? = null
    private val binding get() = _binding!!
    private val companyUserViewModel : CompanyUserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompanyAndUserInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val token = TokenManager().getToken()
        if (token != null) {
            val jwt: DecodedJWT = JWT.decode(token)
            val userRole = jwt.getClaim("role").asString()

            if (userRole != "Admin") {
                binding.btnUsers.visibility = View.GONE
            } else {
                binding.btnUsers.visibility = View.VISIBLE
            }
        } else {
            binding.btnUsers.visibility = View.GONE
        }


        binding.navDrawerButton.drawerToggleButton.setOnClickListener {
            (activity as? CarchiveActivity)?.toggleDrawer()
        }

        binding.btnUsers.setOnClickListener {
            findNavController().navigate(R.id.action_companyUserFragment_to_usersFromCompanyFragment)
        }

        binding.btnNewpass.setOnClickListener {
            val userEmail = companyUserViewModel.user.value.email
            val bundle = Bundle().apply {
                putString("userEmail", userEmail)
            }
            findNavController().navigate(R.id.action_companyUserFragment_to_editUserPasswordFragment, bundle)

        }

        companyUserViewModel.fetchUser()
        companyUserViewModel.fetchCompany()

        lifecycleScope.launch {
            launch {
                companyUserViewModel.user.collect { user ->
                    if (user != UserDto.EMPTY) {
                        binding.tvUserName.text = "Korisnik: ${user.firstName} ${user.lastName}"
                        binding.tvUserEmail.text = "Email: ${user.email}"
                        binding.tvUserID.text = "ID korisnika: ${user.id}"
                    } else {
                        binding.tvUserName.text = "Korisnik: Nepoznato"
                        binding.tvUserEmail.text = "Email: Nepoznato"
                        binding.tvUserID.text = "ID korisnika: Nepoznato"
                    }
                }
            }

            launch {
                companyUserViewModel.company.collect { company ->
                    if (company != CompanyDto.EMPTY) {
                        binding.tvCompanyName.text = "${company.name}"
                        binding.tvCompanyId.text = "ID: ${company.id}"
                        binding.tvCompanyCity.text = "Grad: ${company.city}"
                        binding.tvCompanyAddress.text = "Adresa: ${company.address}"
                        binding.tvCompanyPin.text = "OIB: ${company.pin}"
                    } else {
                        binding.tvCompanyName.text = "Naziv firme: Nepoznato"
                        binding.tvCompanyId.text = "ID: Nepoznato"
                        binding.tvCompanyCity.text = "Grad: Nepoznato"
                        binding.tvCompanyAddress.text = "Adresa: Nepoznato"
                        binding.tvCompanyPin.text = "OIB: Nepoznato"
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as? CarchiveActivity)?.setDrawerEnabled(true)
    }

    override fun onPause() {
        super.onPause()
        (activity as? CarchiveActivity)?.setDrawerEnabled(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}