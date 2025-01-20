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
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.carchive.R
import com.example.carchive.data.dto.InsuranceDto
import com.example.carchive.data.dto.OfferDto
import com.example.carchive.data.dto.ReservationDto
import com.example.carchive.data.network.Result.Error
import com.example.carchive.data.network.Result.Success
import com.example.carchive.databinding.FragmentAddContractBinding
import com.example.carchive.entities.Contact
import com.example.carchive.entities.Vehicle
import com.example.carchive.viewmodels.ContractsViewModel
import kotlinx.coroutines.launch


class AddContractFragment : Fragment() {

    private val viewModel : ContractsViewModel by viewModels()

    private var _binding: FragmentAddContractBinding? = null
    private val binding get() = _binding!!

    private lateinit var buttonTypeSell: Button
    private lateinit var buttonTypeRent: Button
    private lateinit var contractSellLayout: LinearLayout
    private lateinit var contractRentLayout: LinearLayout
    private lateinit var offerSwitch: Switch
    private lateinit var offerLabel: TextView
    private lateinit var offerSpinner: Spinner
    private lateinit var vehicleLabel: TextView
    private lateinit var vehicleSpinner: Spinner
    private lateinit var contactLabel: TextView
    private lateinit var contactSpinner: Spinner
    private lateinit var reservationSpinner: Spinner
    private lateinit var insuranceSpinner: Spinner
    private lateinit var buttonAdd: Button
    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText
    private lateinit var etPlace: EditText

    var selectedOfferId: Int? = null
    var selectedContactId: Int? = null
    var selectedVehicleId: Int? = null
    var selectedReservationId: Int? = null
    var selectedInsuranceId: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private var contractType: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddContractBinding.inflate(inflater, container, false)
        binding.navBackButton.backButton.setOnClickListener(){
            findNavController().popBackStack()
        }

        buttonTypeSell = binding.btnAddContractSell
        buttonTypeRent = binding.btnAddContractRent
        contractSellLayout = binding.contractSellLayout
        contractRentLayout = binding.contractRentLayout
        offerSwitch = binding.swContractAddOffer
        offerLabel = binding.tvContractAddOfferLabel
        offerSpinner = binding.spnContractAddOffer
        vehicleLabel = binding.tvContractAddVehicleLabel
        vehicleSpinner = binding.spnContractAddVehicle
        contactLabel = binding.tvContractAddContactLabel
        contactSpinner = binding.spnContractAddContact
        reservationSpinner = binding.spnContractAddReservation
        insuranceSpinner = binding.spnContractAddInsurance
        buttonAdd = binding.btnContractAdd
        etTitle = binding.etContractAddTitle
        etContent = binding.etContractAddContent
        etPlace = binding.etContractAddPlace

        contractSellLayout.visibility = View.VISIBLE
        contractRentLayout.visibility = View.GONE


        updateContractType(1)
        initListeners()

        buttonAdd.setOnClickListener {
            viewModel.validateContractInputs(
                contractType, selectedOfferId, selectedVehicleId, selectedContactId,
                selectedReservationId, selectedInsuranceId,
                etTitle.text.toString(), etContent.text.toString(), etPlace.text.toString()
            )

            viewModel.validationResult.observe(viewLifecycleOwner) { isValid ->
                if(isValid) {
                    lifecycleScope.launch {
                        viewModel.postContract(
                            contractType, selectedOfferId, selectedVehicleId, selectedContactId,
                            selectedReservationId, selectedInsuranceId,
                            etTitle.text.toString(), etContent.text.toString(), etPlace.text.toString()
                        )

                        viewModel.postResult.observe(viewLifecycleOwner) { result ->
                            when(result) {
                                is Success -> {
                                    Toast.makeText(requireContext(), getString(R.string.ugovorDodan), Toast.LENGTH_SHORT).show()
                                    findNavController().navigate(R.id.action_addContractFragment_to_contractsFragment)
                                }
                                is Error -> {
                                    Toast.makeText(requireContext(), getString(R.string.greskaKodDodavanjaUgovora), Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                } else {
                    Toast.makeText(context, getString(R.string.potrebnoIspunitiPolja), Toast.LENGTH_SHORT).show()
                }
            }
        }


        viewModel.fetchAllData()

        lifecycleScope.launch {
            viewModel.offers.collect { offers ->
                val offerNames = mutableListOf(getString(R.string.odaberitePonudu))
                val offerMap = mutableMapOf<String, OfferDto>()

                offerNames.addAll(offers.map {
                    val displayName = "ID: ${it.id}, ${it.title}"
                    offerMap[displayName] = it
                    displayName
                })

                val offerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, offerNames)
                offerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                offerSpinner.adapter = offerAdapter
                offerSpinner.setSelection(0)

                offerSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        if (position != 0) {
                            val selectedOffer = offerMap[offerNames[position]]
                            selectedOffer?.let {
                                selectedOfferId = it.id
                            }
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            }
        }

        lifecycleScope.launch {
            viewModel.vehicles.collect { vehicles ->
                val vehicleNames = mutableListOf(getString(R.string.odaberiteVozilo))
                val vehicleMap = mutableMapOf<String, Vehicle>()

                vehicleNames.addAll(vehicles.map {
                    val displayName = "${it.registration}, ${it.brand} ${it.model}"
                    vehicleMap[displayName] = it
                    displayName
                })

                val vehicleAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, vehicleNames)
                vehicleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                vehicleSpinner.adapter = vehicleAdapter
                vehicleSpinner.setSelection(0)

                vehicleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        if (position != 0) {
                            val selectedVehicle = vehicleMap[vehicleNames[position]]
                            selectedVehicle?.let {
                                selectedVehicleId = it.id
                            }
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            }
        }

        lifecycleScope.launch {
            viewModel.contacts.collect { contacts ->
                val contactNames = mutableListOf(getString(R.string.odaberiteKontakt))
                val contactMap = mutableMapOf<String, Contact>()

                contactNames.addAll(contacts.map {
                    val displayName = "${it.firstName} ${it.lastName}, ${it.pin}"
                    contactMap[displayName] = it
                    displayName
                })

                val contactAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, contactNames)
                contactAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                contactSpinner.adapter = contactAdapter
                contactSpinner.setSelection(0)

                contactSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        if (position != 0) {
                            val selectedContact = contactMap[contactNames[position]]
                            selectedContact?.let {
                                selectedContactId = it.id
                            }
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            }
        }

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
                reservationSpinner.setSelection(0)

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
                insuranceSpinner.setSelection(0)

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
        return binding.root
    }

    private fun initListeners() {
        buttonTypeSell.setOnClickListener {
            updateContractType(1)
            reservationSpinner.setSelection(0)
            selectedReservationId = null
            insuranceSpinner.setSelection(0)
            selectedInsuranceId = null
        }

        buttonTypeRent.setOnClickListener {
            updateContractType(2)
            offerSpinner.setSelection(0)
            selectedOfferId = null
            contactSpinner.setSelection(0)
            selectedContactId = null
            vehicleSpinner.setSelection(0)
            selectedVehicleId = null
        }

        offerSwitch.setOnCheckedChangeListener { _, isChecked ->
            vehicleSpinner.setSelection(0)
            selectedVehicleId = null
            contactSpinner.setSelection(0)
            selectedContactId = null
            offerLabel.visibility = if (isChecked) View.VISIBLE else View.GONE
            offerSpinner.visibility = if (isChecked) View.VISIBLE else View.GONE
            vehicleLabel.visibility = if (isChecked) View.GONE else View.VISIBLE
            vehicleSpinner.visibility = if (isChecked) View.GONE else View.VISIBLE
            contactSpinner.visibility = if (isChecked) View.GONE else View.VISIBLE
            contactLabel.visibility = if (isChecked) View.GONE else View.VISIBLE
        }
    }

    private fun updateContractType(type: Int) {
        contractType = type

        if(type == 1) {
            contractSellLayout.visibility = View.VISIBLE
            contractRentLayout.visibility = View.GONE
            buttonTypeSell.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.accentColorLight))
            buttonTypeSell.setTextColor(ContextCompat.getColor(requireContext(), R.color.primaryColorLight))
            buttonTypeRent.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primaryColorLight))
            buttonTypeRent.setTextColor(ContextCompat.getColor(requireContext(), R.color.accentColorLight))
        } else {
            contractSellLayout.visibility = View.GONE
            contractRentLayout.visibility = View.VISIBLE
            buttonTypeRent.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.accentColorLight))
            buttonTypeRent.setTextColor(ContextCompat.getColor(requireContext(), R.color.primaryColorLight))
            buttonTypeSell.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primaryColorLight))
            buttonTypeSell.setTextColor(ContextCompat.getColor(requireContext(), R.color.accentColorLight))
        }
    }
}