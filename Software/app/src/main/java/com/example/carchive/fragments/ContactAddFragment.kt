package com.example.carchive.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Switch
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.carchive.R
import com.example.carchive.adapters.ContactsAdapter
import com.example.carchive.entities.Contact
import com.example.carchive.helpers.MockDataLoader

class ContactAddFragment : Fragment() {

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
    private lateinit var spnActivities : Spinner
    private lateinit var spnStates : Spinner
    private lateinit var btnAddContact : Button
    private lateinit var swtOfferState : Switch

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contact_add, container, false)

        etName = view.findViewById(R.id.et_contact_add_name)
        etSurname = view.findViewById(R.id.et_contact_add_surname)
        etPin = view.findViewById(R.id.et_contact_add_pin)
        etAddress = view.findViewById(R.id.et_contact_add_address)
        etPhoneNumber = view.findViewById(R.id.et_contact_add_phone_number)
        etMobileNumber = view.findViewById(R.id.et_contact_add_mobile_number)
        etEmail = view.findViewById(R.id.et_contact_add_email)
        etDescription = view.findViewById(R.id.et_contact_add_description)
        spnCountries = view.findViewById(R.id.spn_contact_add_countries)
        spnCities = view.findViewById(R.id.spn_contact_add_cities)
        spnActivities = view.findViewById(R.id.spn_contact_add_activities)
        spnStates = view.findViewById(R.id.spn_contact_add_states)
        btnAddContact = view.findViewById(R.id.btn_contact_add)

        val countryList = resources.getStringArray(R.array.drzave).toList()
        val cityList = resources.getStringArray(R.array.gradovi).toList()
        val activitiesList = resources.getStringArray(R.array.aktivnosti).toList()
        val statesList = resources.getStringArray(R.array.statusi).toList()

        populateSpinner(spnCountries, countryList)
        populateSpinner(spnCities, cityList)
        populateSpinner(spnActivities, activitiesList)
        populateSpinner(spnStates, statesList)

        btnAddContact.setOnClickListener {
            if (validateInputs()) {
                val newContact = Contact(
                    id = MockDataLoader.getMockContacts().size + 1,
                    firstName = etName.text.toString(),
                    lastName = etSurname.text.toString(),
                    pin = etPin.text.toString(),
                    address = etAddress.text.toString(),
                    phoneNumber = etPhoneNumber.text.toString(),
                    mobilePhoneNumber = etMobileNumber.text.toString(),
                    emailAddress = etEmail.text.toString(),
                    description = etDescription.text.toString(),
                    country = spnCountries.selectedItem.toString(),
                    city = spnCities.selectedItem.toString(),
                    activity = spnActivities.selectedItem.toString(),
                    state = spnStates.selectedItem.toString(),
                    offerSent = swtOfferState.isChecked
                )
                val contactsAdapter = (recyclerView.adapter as ContactsAdapter)
                contactsAdapter.addContact(newContact)
                Toast.makeText(context, "Kontakt uspješno dodan!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Potrebno je ispuniti sva polja!", Toast.LENGTH_SHORT).show()
            }
        }

        return view
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

    private fun validateInputs(): Boolean {
        return etName.text.isNotEmpty() &&
                etSurname.text.isNotEmpty() &&
                etPin.text.isNotEmpty() &&
                etAddress.text.isNotEmpty() &&
                etPhoneNumber.text.isNotEmpty() &&
                etMobileNumber.text.isNotEmpty() &&
                etEmail.text.isNotEmpty() &&
                etDescription.text.isNotEmpty()
    }
}