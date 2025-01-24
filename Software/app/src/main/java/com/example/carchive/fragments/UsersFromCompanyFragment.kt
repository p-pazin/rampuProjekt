package com.example.carchive.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carchive.CarchiveActivity
import com.example.carchive.R
import com.example.carchive.adapters.UsersFromCompanyAdapter
import com.example.carchive.data.dto.UserDto
import com.example.carchive.data.network.Result
import com.example.carchive.databinding.FragmentCompnyUsersBinding
import com.example.carchive.viewmodels.CompanyUserViewModel
import kotlinx.coroutines.flow.observeOn
import kotlinx.coroutines.launch

class UsersFromCompanyFragment : Fragment() {
    private var _binding: FragmentCompnyUsersBinding? = null
    private val binding get() = _binding!!
    private val companyUserViewModel: CompanyUserViewModel by viewModels()
    private lateinit var usersFromCompanyAdapter: UsersFromCompanyAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompnyUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.toolbarButtons.btnDodaj.setOnClickListener {
            findNavController().navigate(R.id.action_usersFromCompanyFragment_to_newComapnyUserFragment)
        }

        binding.toolbarButtons.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        usersFromCompanyAdapter = UsersFromCompanyAdapter(
            emptyList(),
            requireContext()
        ) { user -> deleteUser(user) }

        binding.rvContactList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvContactList.adapter = usersFromCompanyAdapter

        companyUserViewModel.fetchCompanyUsers()

        lifecycleScope.launch {
            companyUserViewModel.companyUsers.collect { users ->
                usersFromCompanyAdapter.updateUsers(users)
            }
        }


        companyUserViewModel.deleteUserResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                        Toast.makeText(requireContext(), "User successfully deleted", Toast.LENGTH_SHORT).show()
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(), "Neuspješno obrisana osoba", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun deleteUser(user: UserDto) {
        companyUserViewModel.deleteUserFromCompany(user.id)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
