package com.example.carchive.fragments

import android.annotation.SuppressLint
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
import androidx.recyclerview.widget.RecyclerView
import com.example.carchive.R
import com.example.carchive.adapters.ContactsAdapter
import com.example.carchive.databinding.FragmentAddCarBinding
import com.example.carchive.databinding.FragmentContactAddBinding
import com.example.carchive.databinding.FragmentContactsBinding
import com.example.carchive.entities.Contact
import com.example.carchive.data.network.Result
import com.example.carchive.data.network.Result.Success
import com.example.carchive.data.network.Result.Error
import com.example.carchive.viewmodels.ContactsViewModel
import kotlinx.coroutines.launch

class ContactAddFragment : Fragment() {

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
    private lateinit var btnAddContact : Button

    private var _binding: FragmentContactAddBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentContactAddBinding.inflate(inflater, container, false)
        binding.navBackButton.backButton.setOnClickListener(){
            findNavController().popBackStack()
        }

        etName = binding.etContactAddName
        etSurname = binding.etContactAddSurname
        etPin = binding.etContactAddPin
        etAddress = binding.etContactAddAddress
        etPhoneNumber = binding.etContactAddPhoneNumber
        etMobileNumber = binding.etContactAddMobileNumber
        etEmail = binding.etContactAddEmail
        etDescription = binding.etContactAddDescription
        spnCountries = binding.spnContactAddCountries
        spnCities = binding.spnContactAddCities
        tvCities = binding.tvContactAddCitiesLabel
        spnStates = binding.spnContactAddStates
        btnAddContact = binding.btnContactAdd

        val countryList = resources.getStringArray(R.array.drzave).toList()
        val cityList = resources.getStringArray(R.array.gradovi).toList()
        val statesList = resources.getStringArray(R.array.statusi).toList()

        populateSpinner(spnCountries, countryList)
        populateSpinner(spnCities, cityList)
        populateSpinner(spnStates, statesList)

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

        btnAddContact.setOnClickListener {
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
            val state = 1

            if(!viewModel.validateInputs(firstName, lastName, pin, address, telephoneNumber,
                    mobileNumber, email, description)) {
                Toast.makeText(context, getString(R.string.potrebnoIspunitiPolja), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            viewModel.postContact(
                firstName, lastName, pin, address, telephoneNumber,
                mobileNumber, email, description, country, city, state)

        }

        viewModel.postResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    Toast.makeText(requireContext(), getString(R.string.kontaktDodan), Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_contactAddFragment_to_contactsFragment)
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(), getString(R.string.greskaKodDodavanjaKontakta), Toast.LENGTH_SHORT).show()
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
}