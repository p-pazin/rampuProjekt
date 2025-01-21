package com.example.carchive.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.carchive.R
import com.example.carchive.data.dto.ContractDetailedRentDto
import com.example.carchive.data.dto.InsuranceDto
import com.example.carchive.data.dto.ReservationDto
import com.example.carchive.data.network.Result.Error
import com.example.carchive.data.network.Result.Success
import com.example.carchive.databinding.FragmentEditRentContractBinding
import com.example.carchive.viewmodels.ContractsViewModel
import kotlinx.coroutines.launch


class EditRentContractFragment : Fragment() {

    private val viewModel : ContractsViewModel by viewModels()

    private var _binding: FragmentEditRentContractBinding? = null
    private val binding get() = _binding!!
    private var contract: ContractDetailedRentDto? = null

    private lateinit var reservationSpinner: Spinner
    private lateinit var insuranceSpinner: Spinner
    private lateinit var signedSpinner: Spinner
    private lateinit var buttonEdit: Button
    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText
    private lateinit var etPlace: EditText

    var selectedReservationId: Int? = null
    var selectedInsuranceId: Int? = null
    var contractSigned: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contract = arguments?.getSerializable("contract_rent_key") as ContractDetailedRentDto

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEditRentContractBinding.inflate(inflater, container, false)
        binding.navBackButton.backButton.setOnClickListener(){
            findNavController().popBackStack()
        }

        reservationSpinner = binding.spnContractRentEditReservation
        insuranceSpinner = binding.spnContractRentEditInsurance
        signedSpinner = binding.spnContractRentEditSigned
        buttonEdit = binding.btnContractRentEdit
        etTitle = binding.etContractRentEditTitle
        etContent = binding.etContractRentEditContent
        etPlace = binding.etContractRentEditPlace

        etTitle.setText(contract?.title)
        etContent.setText(contract?.content)
        etPlace.setText(contract?.place)

        viewModel.fetchAllData()

        initSpinnerValuesFromContract()

        buttonEdit.setOnClickListener {
            viewModel.validateContractInputs(
                2, -1, -1, -1,
                selectedReservationId, selectedInsuranceId,
                etTitle.text.toString(), etContent.text.toString(),
                etPlace.text.toString()
            )

            viewModel.validationResult.observe(viewLifecycleOwner) { isValid ->
                if(isValid) {
                    lifecycleScope.launch {
                        viewModel.putContract(
                            2, -1, -1, -1,
                            selectedReservationId, selectedInsuranceId,
                            etTitle.text.toString(), etContent.text.toString(),
                            etPlace.text.toString(), contract?.id, contractSigned
                        )

                        viewModel.putResult.observe(viewLifecycleOwner) { result ->
                            when(result) {
                                is Success -> {
                                    Toast.makeText(requireContext(), getString(R.string.ugovorAzuriran), Toast.LENGTH_SHORT).show()
                                    findNavController().navigate(R.id.action_editContractRentFragment_to_contractsFragment)
                                }
                                is Error -> {
                                    Toast.makeText(requireContext(), getString(R.string.greskaKodAzuriranjaUgovora), Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                } else {
                    Toast.makeText(context, getString(R.string.potrebnoIspunitiPolja), Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding.root
    }

    private fun initSpinnerValuesFromContract() {
        contract?.let { contractData ->
            lifecycleScope.launch {
                viewModel.reservations.collect { reservations ->
                    val reservationNames = mutableListOf(getString(R.string.odaberiteRezervaciju))
                    val reservationMap = mutableMapOf<String, ReservationDto>()

                    reservationNames.addAll(reservations.map {
                        val displayName = "ID: ${it.id}, ${it.startDate} - ${it.endDate}"
                        reservationMap[displayName] = it
                        displayName
                    })

                    val reservationAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, reservationNames)
                    reservationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    reservationSpinner.adapter = reservationAdapter

                    val selectedReservationName = reservationNames.find { it.contains("ID: ${contractData.reservationId}") }
                    val selectedReservationPosition = reservationNames.indexOf(selectedReservationName)
                    reservationSpinner.setSelection(selectedReservationPosition)

                    if (selectedReservationPosition > 0) {
                        val preSelectedReservation = reservationMap[selectedReservationName]
                        preSelectedReservation?.let {
                            selectedReservationId = it.id
                        }
                    }

                    reservationSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            if (position != 0) {
                                val selectedReservation = reservationMap[reservationNames[position]]
                                selectedReservation?.let {
                                    selectedReservationId = it.id
                                }
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }
                }
            }

            lifecycleScope.launch {
                viewModel.insurances.collect { insurances ->
                    val insuranceNames = mutableListOf(getString(R.string.odaberiteOsiguranje))
                    val insuranceMap = mutableMapOf<String, InsuranceDto>()

                    insuranceNames.addAll(insurances.map {
                        val displayName = it.name
                        insuranceMap[displayName] = it
                        displayName
                    })

                    val insuranceAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, insuranceNames)
                    insuranceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    insuranceSpinner.adapter = insuranceAdapter

                    val selectedInsuranceName = insuranceNames.find { it == contractData.nameInsurance }
                    val selectedInsurancePosition = insuranceNames.indexOf(selectedInsuranceName)
                    insuranceSpinner.setSelection(selectedInsurancePosition)

                    if(selectedInsurancePosition > 0) {
                        val preSelectedInsurance = insuranceMap[selectedInsuranceName]
                        preSelectedInsurance?.let {
                            selectedInsuranceId = it.id
                        }
                    }

                    insuranceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            if (position != 0) {
                                val selectedInsurance = insuranceMap[insuranceNames[position]]
                                selectedInsurance?.let {
                                    selectedInsuranceId = it.id
                                }
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                val signedOptions = listOf("Potpisan", "Nije potpisan")
                val signedAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, signedOptions)
                signedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                signedSpinner.adapter = signedAdapter

                val preSelectedPosition = if(contractData.signed == 1) 0 else 1
                signedSpinner.setSelection(preSelectedPosition)

                signedSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val isSigned = if (position == 0) 1 else 0
                        contractSigned = isSigned
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            }
        }
    }
}