package com.example.carchive.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carchive.CarchiveActivity
import com.example.carchive.R
import com.example.carchive.adapters.ContractsAdapter
import com.example.carchive.data.dto.ContractDto
import com.example.carchive.databinding.FragmentContractsBinding
import com.example.carchive.viewmodels.ContractsViewModel
import kotlinx.coroutines.launch

class ContractsFragment : Fragment() {
    private val viewModel : ContractsViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private var _binding: FragmentContractsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContractsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnDodaj = binding.sidebarLogo.btnDodaj

        recyclerView = binding.rvContractList
        val adapter = ContractsAdapter(emptyList()) { contract: ContractDto ->
            val bundle = Bundle().apply {
                putInt("contract_id", contract.id)
                putInt("contract_type", contract.type)
            }
            findNavController().navigate(R.id.action_contractsFragment_to_contractDetailsFragment, bundle)
        }
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = adapter

        binding.sidebarLogo.drawerToggleButton.setOnClickListener {
            (activity as? CarchiveActivity)?.toggleDrawer()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.contracts.collect { contracts ->
                adapter.updateItems(contracts)
            }
        }

        viewModel.fetchContracts()
    }

    override fun onResume() {
        super.onResume()
        (activity as? CarchiveActivity)?.setDrawerEnabled(true)
    }

    override fun onPause() {
        super.onPause()
        (activity as? CarchiveActivity)?.setDrawerEnabled(false)
    }
}