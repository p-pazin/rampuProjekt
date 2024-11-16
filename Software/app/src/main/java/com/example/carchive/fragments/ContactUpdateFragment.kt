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
import com.example.carchive.entities.User
import com.example.carchive.helpers.MockDataLoader

class ContactUpdateFragment : Fragment() {

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
    private lateinit var btnUpdateContact : Button
    private lateinit var swtOfferState : Switch

    private lateinit var contact: Contact

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contact_update, container, false)

        etName = view.findViewById(R.id.et_contact_update_name)
        etSurname = view.findViewById(R.id.et_contact_update_surname)
        etPin = view.findViewById(R.id.et_contact_update_pin)
        etAddress = view.findViewById(R.id.et_contact_update_address)
        etPhoneNumber = view.findViewById(R.id.et_contact_update_phone_number)
        etMobileNumber = view.findViewById(R.id.et_contact_update_mobile_number)
        etEmail = view.findViewById(R.id.et_contact_update_email)
        etDescription = view.findViewById(R.id.et_contact_update_description)
        spnCountries = view.findViewById(R.id.spn_contact_update_countries)
        spnCities = view.findViewById(R.id.spn_contact_update_cities)
        spnActivities = view.findViewById(R.id.spn_contact_update_activities)
        spnStates = view.findViewById(R.id.spn_contact_update_states)
        btnUpdateContact = view.findViewById(R.id.btn_contact_update)
        swtOfferState = view.findViewById(R.id.swt_contact_update_offer_state)

        val countryList = resources.getStringArray(R.array.drzave).toList()
        val cityList = resources.getStringArray(R.array.gradovi).toList()
        val activitiesList = resources.getStringArray(R.array.aktivnosti).toList()
        val statesList = resources.getStringArray(R.array.statusi).toList()

        populateSpinner(spnCountries, countryList)
        populateSpinner(spnCities, cityList)
        populateSpinner(spnActivities, activitiesList)
        populateSpinner(spnStates, statesList)

        contact = MockDataLoader.getMockContacts()[0]
        populateInputs()

        btnUpdateContact.setOnClickListener {
            if (validateInputs()) {
                val updatedContact = Contact(
                    id = contact.id,
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
                contactsAdapter.updateContact(updatedContact, updatedContact.id)
                Toast.makeText(context, getString(R.string.kontaktAzuriran), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, getString(R.string.potrebnoIspunitiPolja), Toast.LENGTH_SHORT).show()
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

    private fun populateInputs() {
        etName.setText(contact.firstName)
        etSurname.setText(contact.lastName)
        etPin.setText(contact.pin)
        spnCountries.setSelection(resources.getStringArray(R.array.drzave).toList().indexOfFirst { it == contact.country })
        spnCities.setSelection(resources.getStringArray(R.array.gradovi).toList().indexOfFirst { it == contact.city })
        etAddress.setText(contact.address)
        etPhoneNumber.setText(contact.phoneNumber)
        etMobileNumber.setText(contact.mobilePhoneNumber)
        etEmail.setText(contact.emailAddress)
        etDescription.setText(contact.description)
        spnActivities.setSelection(resources.getStringArray(R.array.aktivnosti).toList().indexOfFirst { it == contact.activity })
        spnStates.setSelection(resources.getStringArray(R.array.statusi).toList().indexOfFirst { it == contact.state })
        swtOfferState.isChecked = contact.offerSent
    }
}