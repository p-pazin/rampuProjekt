package com.example.carchive.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.carchive.R
import com.example.carchive.data.dto.InvoiceDtoPost
import com.example.carchive.databinding.FragmentAddInvoiceBinding
import com.example.carchive.viewmodels.ContractsViewModel
import com.example.carchive.viewmodels.InvoiceViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate

class AddInvoiceFragment : Fragment() {

    private val viewModelInvoice: InvoiceViewModel by viewModels()
    private val viewModelContract: ContractsViewModel by viewModels()

    private var _binding: FragmentAddInvoiceBinding? = null
    private val binding get() = _binding!!

    private var selectedContractRent: Int? = null
    private var selectedContractSell: Int? = null

    private var invoiceType: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddInvoiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.navBackButton.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModelContract.fetchContracts()
        setupSpinners()
        setupListeners()
        updateInvoiceType(1)
    }

    private fun setupSpinners() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelContract.contracts.collect { contracts ->
                    val filteredContracts = if (invoiceType == 1) {
                        contracts.filter { it.type == 1 }
                    } else {
                        contracts.filter { it.type == 2 }
                    }

                    val contractNames = filteredContracts.map { "${it.title} - ${it.contactName}" }
                    val adapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        contractNames
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    if (invoiceType == 1) {
                        binding.spnInvoiceAddContract.adapter = adapter
                    } else {
                        binding.spnInvoiceAddContractRent.adapter = adapter
                    }
                }
            }
        }
    }

    private fun setupListeners() {
        binding.btnAddInvoiceSell.setOnClickListener {
            updateInvoiceType(1)
        }

        binding.btnAddInvoiceRent.setOnClickListener {
            updateInvoiceType(2)
        }

        binding.btnInvoiceAdd.setOnClickListener {
            validateAndSubmitInvoice()
        }

        viewModelInvoice.validationResult.observe(viewLifecycleOwner) { isValid ->
            if (isValid) {
                submitInvoice()
            } else {
                Toast.makeText(requireContext(), R.string.info_missing, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateAndSubmitInvoice() {
        val selectedContractIndex = binding.spnInvoiceAddContract.selectedItemPosition
        selectedContractSell = viewModelContract.contracts.value.getOrNull(selectedContractIndex)?.id

        val paymentMethodSell = binding.etInvoiceAddPaymentMethod.text.toString()
        val vatSell = binding.etInvoiceAddVat.text.toString()
        val totalSell = binding.etInvoiceAddTotalCost.text.toString()

        val selectedContractIndexRent = binding.spnInvoiceAddContractRent.selectedItemPosition
        selectedContractRent = viewModelContract.contracts.value.getOrNull(selectedContractIndexRent)?.id
   val paymentMethodRent = binding.etInvoiceAddPaymentMethodRent.text.toString()
        val vatRent = binding.etInvoiceAddVatRent.text.toString()
        val totalRent = binding.etInvoiceAddTotalCostRent.text.toString()
        val mileage = binding.etInvoiceAddMileageRent.text.toString()

        if (invoiceType == 1) {
            viewModelInvoice.validateInvoiceInputs(
                invoiceType,
                null,
                selectedContractSell,
                null,
                null,
                null,
                null,
                paymentMethodSell,
                vatSell,
                totalSell
            )
        } else {
            viewModelInvoice.validateInvoiceInputs(
                invoiceType,
                selectedContractRent,
                null,
                paymentMethodRent,
                vatRent,
                totalRent,
                mileage,
                null,
                null,
                null
            )
        }
    }

    private fun submitInvoice() {
        val currentDate = LocalDate.now()
        if (invoiceType == 1) {
            val selectedContractIndex = binding.spnInvoiceAddContract.selectedItemPosition
            selectedContractSell = viewModelContract.contracts.value.getOrNull(selectedContractIndex)?.id
            val invoiceSell = InvoiceDtoPost(
                dateOfCreation = currentDate.toString(),
                paymentMethod = binding.etInvoiceAddPaymentMethod.text.toString(),
                vat = binding.etInvoiceAddVat.text.toString().toDouble(),
                totalCost = binding.etInvoiceAddTotalCost.text.toString().toDouble(),
                mileage = 0
            )
            lifecycleScope.launch {
                viewModelInvoice.postInvoice(
                    invoiceType,
                    selectedContractSell!!,
                    invoiceSell
                )
            }
        } else {
            val selectedContractIndexRent = binding.spnInvoiceAddContractRent.selectedItemPosition
            selectedContractRent = viewModelContract.contracts.value.getOrNull(selectedContractIndexRent)?.id
            val invoiceRent = InvoiceDtoPost(
                dateOfCreation = currentDate.toString(),
                paymentMethod = binding.etInvoiceAddPaymentMethodRent.text.toString(),
                vat = binding.etInvoiceAddVatRent.text.toString().toDouble(),
                totalCost = binding.etInvoiceAddTotalCostRent.text.toString().toDouble(),
                mileage = binding.etInvoiceAddMileageRent.text.toString().toInt()
            )
            lifecycleScope.launch {
                viewModelInvoice.postInvoice(
                    invoiceType,
                    selectedContractRent ?: selectedContractSell!!,
                    invoiceRent
                )
            }
        }
    }

    private fun updateInvoiceType(type: Int) {
        invoiceType = type

        if (type == 1) {
            binding.invoiceSellLayout.visibility = View.VISIBLE
            binding.invoiceRentLayout.visibility = View.GONE
            binding.btnAddInvoiceSell.setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.accentColorLight)
            )
            binding.btnAddInvoiceSell.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.primaryColorLight)
            )
            binding.btnAddInvoiceRent.setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.primaryColorLight)
            )
            binding.btnAddInvoiceRent.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.accentColorLight)
            )
        } else {
            binding.invoiceSellLayout.visibility = View.GONE
            binding.invoiceRentLayout.visibility = View.VISIBLE
            binding.btnAddInvoiceRent.setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.accentColorLight)
            )
            binding.btnAddInvoiceRent.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.primaryColorLight)
            )
            binding.btnAddInvoiceSell.setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.primaryColorLight)
            )
            binding.btnAddInvoiceSell.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.accentColorLight)
            )
        }

        setupSpinners()
    }
}
