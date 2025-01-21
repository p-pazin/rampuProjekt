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
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.carchive.R
import com.example.carchive.data.dto.ContractDetailedSaleDto
import com.example.carchive.data.dto.OfferDto
import com.example.carchive.data.network.Result.Error
import com.example.carchive.data.network.Result.Success
import com.example.carchive.databinding.FragmentEditSaleContractBinding
import com.example.carchive.entities.Contact
import com.example.carchive.entities.Vehicle
import com.example.carchive.viewmodels.ContractsViewModel
import kotlinx.coroutines.launch


class EditSaleContractFragment : Fragment() {

    private val viewModel : ContractsViewModel by viewModels()

    private var _binding: FragmentEditSaleContractBinding? = null
    private val binding get() = _binding!!
    private var contract: ContractDetailedSaleDto? = null

    private lateinit var offerSwitch: Switch
    private lateinit var offerLabel: TextView
    private lateinit var offerSpinner: Spinner
    private lateinit var vehicleLabel: TextView
    private lateinit var vehicleSpinner: Spinner
    private lateinit var contactLabel: TextView
    private lateinit var contactSpinner: Spinner
    private lateinit var signedSpinner: Spinner
    private lateinit var buttonEdit: Button
    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText
    private lateinit var etPlace: EditText

    var selectedOfferId: Int? = null
    var selectedContactId: Int? = null
    var selectedVehicleId: Int? = null
    var contractSigned: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contract = arguments?.getSerializable("contract_sale_key") as ContractDetailedSaleDto

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEditSaleContractBinding.inflate(inflater, container, false)
        binding.navBackButton.backButton.setOnClickListener(){
            findNavController().popBackStack()
        }

        offerSwitch = binding.swContractSellEditOffer
        offerLabel = binding.tvContractSellEditOfferLabel
        offerSpinner = binding.spnContractSellEditOffer
        vehicleLabel = binding.tvContractSellEditVehicleLabel
        vehicleSpinner = binding.spnContractSellEditVehicle
        signedSpinner = binding.spnContractSellEditSigned
        contactLabel = binding.tvContractSellEditContactLabel
        contactSpinner = binding.spnContractSellEditContact
        buttonEdit = binding.btnContractSellEdit
        etTitle = binding.etContractSellEditTitle
        etContent = binding.etContractSellEditContent
        etPlace = binding.etContractSellEditPlace

        etTitle.setText(contract?.title)
        etContent.setText(contract?.content)
        etPlace.setText(contract?.place)

        if(contract?.offerId != 0) {
            offerSwitch.isChecked = true
            selectedOfferId = contract?.offerId

            offerLabel.visibility = View.VISIBLE
            offerSpinner.visibility = View.VISIBLE
            vehicleLabel.visibility = View.GONE
            vehicleSpinner.visibility = View.GONE
            contactLabel.visibility = View.GONE
            contactSpinner.visibility = View.GONE
        } else {
            offerSwitch.isChecked = false
            selectedOfferId = null

            offerLabel.visibility = View.GONE
            offerSpinner.visibility = View.GONE
            vehicleLabel.visibility = View.VISIBLE
            vehicleSpinner.visibility = View.VISIBLE
            contactLabel.visibility = View.VISIBLE
            contactSpinner.visibility = View.VISIBLE
        }

        viewModel.fetchAllData()

        initSpinnerValuesFromContract()
        initListeners()

        buttonEdit.setOnClickListener {
            viewModel.validateContractInputs(
                1, selectedOfferId, selectedVehicleId, selectedContactId,
                -1, -1,
                etTitle.text.toString(), etContent.text.toString(),
                etPlace.text.toString()
            )

            viewModel.validationResult.observe(viewLifecycleOwner) { isValid ->
                if(isValid) {
                    lifecycleScope.launch {
                        viewModel.putContract(
                            1, selectedOfferId, selectedVehicleId, selectedContactId,
                            -1, -1,
                            etTitle.text.toString(), etContent.text.toString(),
                            etPlace.text.toString(), contract?.id, contractSigned
                        )

                        viewModel.putResult.observe(viewLifecycleOwner) { result ->
                            when(result) {
                                is Success -> {
                                    Toast.makeText(requireContext(), getString(R.string.ugovorAzuriran), Toast.LENGTH_SHORT).show()
                                    findNavController().navigate(R.id.action_editContractSaleFragment_to_contractsFragment)
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

    private fun initListeners() {
        offerSwitch.setOnCheckedChangeListener { _, isChecked ->
            vehicleSpinner.setSelection(0)
            selectedVehicleId = null
            contactSpinner.setSelection(0)
            selectedContactId = null
            offerSpinner.setSelection(0)
            selectedOfferId = null
            offerLabel.visibility = if (isChecked) View.VISIBLE else View.GONE
            offerSpinner.visibility = if (isChecked) View.VISIBLE else View.GONE
            vehicleLabel.visibility = if (isChecked) View.GONE else View.VISIBLE
            vehicleSpinner.visibility = if (isChecked) View.GONE else View.VISIBLE
            contactSpinner.visibility = if (isChecked) View.GONE else View.VISIBLE
            contactLabel.visibility = if (isChecked) View.GONE else View.VISIBLE
        }
    }

    private fun initSpinnerValuesFromContract() {
        contract?.let { contractData ->
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.offers.collect { offers ->
                    val offerNames = mutableListOf(getString(R.string.odaberitePonudu))
                    val offerMap = offers.associateBy { "ID: ${it.id}, ${it.title}" }

                    offerNames.addAll(offerMap.keys)
                    val offerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, offerNames)
                    offerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    offerSpinner.adapter = offerAdapter

                    val selectedOfferName = offerNames.find { it.contains("ID: ${contractData.offerId}") }
                    val selectedOfferPosition = offerNames.indexOf(selectedOfferName).takeIf { it >= 0 } ?: 0
                    offerSpinner.setSelection(selectedOfferPosition)

                    if(selectedOfferPosition > 0) {
                        val preSelectedOffer = offerMap[selectedOfferName]
                        preSelectedOffer?.let {
                            selectedOfferId = it.id
                        }
                    }

                    offerSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            selectedOfferId = offerMap[offerNames.getOrNull(position)]?.id
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

                    val selectedVehicleName = vehicleNames.find { it.contains(contractData.vehicle?.registration.toString()) }
                    val selectedVehiclePosition = vehicleNames.indexOf(selectedVehicleName)
                    vehicleSpinner.setSelection(selectedVehiclePosition)

                    if(selectedVehiclePosition > 0) {
                        val preSelectedVehicle = vehicleMap[selectedVehicleName]
                        preSelectedVehicle?.let {
                            selectedVehicleId = it.id
                        }
                    }

                    vehicleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            if(position != 0) {
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

                    val selectedContactName = contactNames.find { it.contains(contractData.contactPin) }
                    val selectedContactPosition = contactNames.indexOf(selectedContactName)
                    contactSpinner.setSelection(selectedContactPosition)

                    if(selectedContactPosition > 0) {
                        val preSelectedContact = contactMap[selectedContactName]
                        preSelectedContact?.let {
                            selectedContactId = it.id
                        }
                    }

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