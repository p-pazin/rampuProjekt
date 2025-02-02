package com.example.carchive.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
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
import com.example.carchive.data.dto.ContractDto
import com.example.carchive.data.dto.InvoiceDtoPost
import com.example.carchive.data.network.Result.Error
import com.example.carchive.data.network.Result.Success
import com.example.carchive.databinding.FragmentAddInvoiceBinding
import com.example.carchive.databinding.ItemSelectedVehicleBinding
import com.example.carchive.viewmodels.ContractsViewModel
import com.example.carchive.viewmodels.InvoiceViewModel
import com.example.carchive.viewmodels.PenaltyViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate

class AddInvoiceFragment : Fragment() {

    private val viewModelInvoice: InvoiceViewModel by viewModels()
    private val viewModelContract: ContractsViewModel by viewModels()
    private val viewModelPenalty: PenaltyViewModel by viewModels()

    private var _binding: FragmentAddInvoiceBinding? = null
    private val binding get() = _binding!!

    private var selectedContractRent: Int? = null
    private var selectedContractSell: Int? = null
    private var selectedContractSigned: Int? = null
    private val selectedPenalties = mutableListOf<String>()
    private var filteredContracts = listOf<ContractDto>()

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
        viewModelPenalty.fetchPenalties()
        setupSpinners()
        setupPaymentMethodSpinner()
        setupListeners()
        updateInvoiceType(1)

        binding.btnAddPenalty.setOnClickListener {
            val selectedPenalty = binding.spinnerPenalties.selectedItem?.toString()
            if (selectedPenalty != null && !selectedPenalties.contains(selectedPenalty)) {
                selectedPenalties.add(selectedPenalty)
                addVehicleToLayout(selectedPenalty)
            } else {
                Toast.makeText(requireContext(), "Penal već dodan!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupSpinners() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelContract.contracts.collect { contracts ->
                    filteredContracts = if (invoiceType == 1) {
                        contracts.filter { it.type == 1 && it.signed == 0 }
                    } else {
                        contracts.filter { it.type == 2 }
                    }

                    val contractNames = filteredContracts.map { "${it.title} - ${it.contactName}" }

                    val saleAdapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        contractNames
                    )
                    saleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spnInvoiceAddContract.adapter = saleAdapter

                    val rentAdapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        contractNames
                    )
                    rentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spnInvoiceAddContractRent.adapter = rentAdapter

                    binding.spnInvoiceAddContract.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                val selectedContract = filteredContracts.getOrNull(position)
                                selectedContract?.let {
                                    updateLayoutBasedOnContract(it)
                                }
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {}
                        }

                    binding.spnInvoiceAddContractRent.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                val selectedContract = filteredContracts.getOrNull(position)
                                selectedContract?.let {
                                    updateLayoutBasedOnContract(it)
                                }
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {}
                        }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelPenalty.penalties.collect { penalties ->
                    val penaltyNames = penalties.map { it.name }
                    val penaltyAdapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        penaltyNames
                    )
                    penaltyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerPenalties.adapter = penaltyAdapter
                }
            }
        }
    }

    private fun addVehicleToLayout(vehicle: String) {
        val vehicleBinding = ItemSelectedVehicleBinding.inflate(LayoutInflater.from(requireContext()), binding.llSelectedPenalties, false)

        vehicleBinding.tvVehicleName.text = vehicle

        vehicleBinding.btnRemoveVehicle.setOnClickListener {
            binding.llSelectedPenalties.removeView(vehicleBinding.root)
            selectedPenalties.remove(vehicle)
        }

        binding.llSelectedPenalties.addView(vehicleBinding.root)
    }

    private fun updateLayoutBasedOnContract(contract: ContractDto) {
        if (invoiceType == 1) {
            binding.invoiceSellLayout.visibility = View.VISIBLE
            binding.invoiceRentLayout.visibility = View.GONE
            binding.invoiceRentLayoutFinal.visibility = View.GONE
        } else {
            binding.invoiceSellLayout.visibility = View.GONE
            binding.invoiceRentLayout.visibility = View.VISIBLE

            if(contract.signed == 1) {
                binding.invoiceRentLayoutFinal.visibility = View.VISIBLE
            }
            else {
                binding.invoiceRentLayoutFinal.visibility = View.GONE
            }
        }
    }


    private fun setupPaymentMethodSpinner() {
        val paymentMethods = listOf("Gotovina", "Kartica", "Uplata na račun")
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, paymentMethods)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spInvoiceAddPaymentMethod.adapter = spinnerAdapter
        binding.spInvoiceAddPaymentMethodRent.adapter = spinnerAdapter
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
        selectedContractSell = filteredContracts.getOrNull(selectedContractIndex)?.id

        val paymentMethodSell = binding.spInvoiceAddPaymentMethod.selectedItem.toString()
        val vatSell = binding.etInvoiceAddVat.text.toString()
        val totalSell = binding.etInvoiceAddTotalCost.text.toString()

        val selectedContractIndexRent = binding.spnInvoiceAddContractRent.selectedItemPosition
        selectedContractRent = filteredContracts.getOrNull(selectedContractIndexRent)?.id
        selectedContractSigned = filteredContracts.getOrNull(selectedContractIndexRent)?.signed

        val paymentMethodRent = binding.spInvoiceAddPaymentMethodRent.selectedItem.toString()
        val vatRent = binding.etInvoiceAddVatRent.text.toString()
        val mileage = binding.etInvoiceFinalAddMileageRent.text.toString()

        if (invoiceType == 1) {
            viewModelInvoice.validateInvoiceInputs(
                invoiceType,
                null,
                selectedContractSell,
                null,
                null,
                null,
                paymentMethodSell,
                vatSell,
                totalSell
            )
        } else {
            if (selectedContractSigned == 0) {
                viewModelInvoice.validateInvoiceInputs(
                    invoiceType,
                    selectedContractRent,
                    null,
                    paymentMethodRent,
                    vatRent,
                    mileage,
                    null,
                    null,
                    null,
                    isRentEnd = true
                )
            } else {
                viewModelInvoice.validateInvoiceInputs(
                    invoiceType,
                    selectedContractRent,
                    null,
                    paymentMethodRent,
                    vatRent,
                    null,
                    null,
                    null,
                    null,
                    isRentEnd = false
                )
            }
        }
    }

    private fun submitInvoice() {
        val currentDate = LocalDate.now()

        if (invoiceType == 1) {
            val selectedContractIndex = binding.spnInvoiceAddContract.selectedItemPosition
            selectedContractSell = viewModelContract.contracts.value.getOrNull(selectedContractIndex)?.id

            val invoiceSell = InvoiceDtoPost(
                dateOfCreation = currentDate.toString(),
                paymentMethod = binding.spInvoiceAddPaymentMethod.selectedItem.toString(),
                vat = binding.etInvoiceAddVat.text.toString().toDouble(),
                mileage = 0
            )

            lifecycleScope.launch {
                viewModelInvoice.postInvoice(
                    invoiceType,
                    selectedContractSell!!,
                    invoiceSell
                )
            }
            lifecycleScope.launch {
                viewModelInvoice.invoiceSale.observe(viewLifecycleOwner) { result ->
                    when(result) {
                        is Success -> {
                            Toast.makeText(requireContext(), "Uspješno dodan racun", Toast.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                        }
                        is Error -> {
                            Toast.makeText(requireContext(), "Greška kod dodavanja racuna", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        } else {
            val selectedContractIndexRent = binding.spnInvoiceAddContractRent.selectedItemPosition
            selectedContractRent = filteredContracts.getOrNull(selectedContractIndexRent)?.id

            val penaltiesIds = getSelectedPenaltiesIds()

            val invoiceRent = InvoiceDtoPost(
                dateOfCreation = currentDate.toString(),
                paymentMethod = binding.spInvoiceAddPaymentMethodRent.selectedItem.toString(),
                vat = binding.etInvoiceAddVatRent.text.toString().toDouble(),
                mileage = binding.etInvoiceFinalAddMileageRent.text.toString().toIntOrNull() ?: 0
            )

            lifecycleScope.launch {
                if (penaltiesIds.isNotEmpty()) {
                    viewModelInvoice.postInvoiceRentEnd(
                        invoiceType,
                        selectedContractRent ?: selectedContractSell!!,
                        invoiceRent,
                        penaltiesIds
                    )
                } else {
                    viewModelInvoice.postInvoiceRentStart(
                        invoiceType,
                        selectedContractRent ?: selectedContractSell!!,
                        invoiceRent
                    )
                }
            }
            lifecycleScope.launch {
                viewModelInvoice.invoiceRentStart.observe(viewLifecycleOwner) { result ->
                    when(result) {
                        is Success -> {
                            Toast.makeText(requireContext(), "Uspješno dodan racun", Toast.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                        }
                        is Error -> {
                            Toast.makeText(requireContext(), "Greška kod dodavanja racuna", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            lifecycleScope.launch {
                viewModelInvoice.invoiceRentEnd.observe(viewLifecycleOwner) { result ->
                    when(result) {
                        is Success -> {
                            Toast.makeText(requireContext(), "Uspješno dodan racun", Toast.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                        }
                        is Error -> {
                            Toast.makeText(requireContext(), "Greška kod dodavanja racuna", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun getSelectedPenaltiesIds(): List<Int> {
        val penaltiesList = viewModelPenalty.penalties.value
        return selectedPenalties.mapNotNull { penaltyName ->
            penaltiesList.find { it.name == penaltyName }?.id
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
