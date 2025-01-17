package com.example.carchive.fragments

import android.os.Bundle
import android.util.Log
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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.carchive.R
import com.example.carchive.adapters.ContactsAdapter
import com.example.carchive.data.network.Result
import com.example.carchive.databinding.FragmentContactDetailsBinding
import com.example.carchive.databinding.FragmentContactUpdateBinding
import com.example.carchive.entities.Contact
import com.example.carchive.entities.User

class ContactUpdateFragment : Fragment() {

    private val viewModel : ContactsViewModel by viewModels()
    private lateinit var etName: EditText
    private lateinit var etSurname: EditText
    private lateinit var etPin: EditText
    private lateinit var etAddress: EditText
    private lateinit var etPhoneNumber: EditText
    private lateinit var etMobileNumber: EditText
    private lateinit var etEmail: EditText
    private lateinit var etDescription: EditText
    private lateinit var spnCountries: Spinner
    private lateinit var spnCities : Spinner
    private lateinit var tvCities : TextView
    private lateinit var spnStates : Spinner
    private lateinit var btnUpdateContact : Button
    private var _binding: FragmentContactUpdateBinding? = null
    private val binding get() = _binding!!
    private var contact: Contact? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contact = arguments?.getSerializable("contact_key") as? Contact
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentContactUpdateBinding.inflate(inflater, container, false)
        binding.navBackButton.backButton.setOnClickListener(){
            findNavController().popBackStack()
        }

        etName = binding.etContactUpdateName
        etSurname = binding.etContactUpdateSurname
        etPin = binding.etContactUpdatePin
        etAddress = binding.etContactUpdateAddress
        etPhoneNumber = binding.etContactUpdatePhoneNumber
        etMobileNumber = binding.etContactUpdateMobileNumber
        etEmail = binding.etContactUpdateEmail
        etDescription = binding.etContactUpdateDescription
        spnCountries = binding.spnContactUpdateCountries
        spnCities = binding.spnContactUpdateCities
        tvCities = binding.tvContactUpdateCitiesLabel
        spnStates = binding.spnContactUpdateStates
        btnUpdateContact = binding.btnContactUpdate

        val countryList = resources.getStringArray(R.array.drzave).toList()
        val cityList = resources.getStringArray(R.array.gradovi).toList()
        val statesList = resources.getStringArray(R.array.statusi).toList()

        populateSpinner(spnCountries, countryList)
        populateSpinner(spnCities, cityList)
        populateSpinner(spnStates, statesList)

        populateInputs()

        spnCountries.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCountry = spnCountries.selectedItem.toString()
                if (selectedCountry == "Hrvatska") {
                    spnCities.visibility = View.VISIBLE
                    tvCities.visibility = View.VISIBLE
                } else {
                    spnCities.visibility = View.GONE
                    tvCities.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                spnCities.visibility = View.GONE
                tvCities.visibility = View.GONE

            }
        }

        btnUpdateContact.setOnClickListener {
            val firstName = etName.text.toString()
            val lastName = etSurname.text.toString()
            val pin = etPin.text.toString()
            val address = etAddress.text.toString()
            val telephoneNumber = etPhoneNumber.text.toString()
            val mobileNumber = etMobileNumber.text.toString()
            val email = etEmail.text.toString()
            val description = etDescription.text.toString()
            val country = spnCountries.selectedItem.toString()
            val city = if(spnCities.visibility == View.VISIBLE) {
                spnCities.selectedItem.toString()
            } else {
                ""
            }
            val state = spnStates.selectedItem.toString()

            if(!viewModel.validateInputs(firstName, lastName, pin, address, telephoneNumber,
                    mobileNumber, email, description)) {
                Toast.makeText(context, getString(R.string.potrebnoIspunitiPolja), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            viewModel.putContact(
                firstName, lastName, pin, address, telephoneNumber,
                mobileNumber, email, description, country, city, state,
                contact!!.dateOfCreation, contact!!.id)

            }

            viewModel.putResult.observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Result.Success -> {
                        Toast.makeText(requireContext(), getString(R.string.kontaktAzuriran), Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_contactUpdateFragment_to_contactsFragment)
                    }
                    is Result.Error -> {
                        Toast.makeText(requireContext(), getString(R.string.greskaKodAzuriranja), Toast.LENGTH_SHORT).show()
                    }
                }
            }

        return binding.root
    }


    private fun populateSpinner(spinner: Spinner, dataList: List<String>) {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            dataList
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun populateInputs() {
        var contactState = if (contact?.state == 1) "Aktivni kontakt" else "Neaktivni kontakt"

        etName.setText(contact?.firstName)
        etSurname.setText(contact?.lastName)
        etPin.setText(contact?.pin)
        spnCountries.setSelection(resources.getStringArray(R.array.drzave).toList().indexOfFirst { it == contact?.country })
        spnCities.setSelection(resources.getStringArray(R.array.gradovi).toList().indexOfFirst { it == contact?.city })
        spnStates.setSelection(resources.getStringArray(R.array.statusi).toList().indexOfFirst { it == contactState })
        etAddress.setText(contact?.address)
        etPhoneNumber.setText(contact?.telephoneNumber)
        etMobileNumber.setText(contact?.mobileNumber)
        etEmail.setText(contact?.email)
        etDescription.setText(contact?.description)
    }
}