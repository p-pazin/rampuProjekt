package com.example.carchive.fragments

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.carchive.data.dto.OfferDto
import com.example.carchive.data.dto.OfferPostDto
import com.example.carchive.databinding.FragmentAddOfferBinding
import com.example.carchive.databinding.FragmentEditOfferBinding
import com.example.carchive.databinding.ItemSelectedVehicleBinding
import com.example.carchive.viewmodels.ContactsViewModel
import com.example.carchive.viewmodels.OfferViewModel
import com.example.carchive.viewmodels.VehicleCatalogViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditOfferFragment: Fragment() {
    private val viewModelOffer: OfferViewModel by viewModels()
    private val viewModelContact: ContactsViewModel by viewModels()
    private val viewModelVehicle: VehicleCatalogViewModel by viewModels()
    private var _binding: FragmentEditOfferBinding? = null
    private val binding get()  = _binding!!

    private val selectedVehicles = mutableListOf<String>()
    private val initialVehicles = mutableSetOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditOfferBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.navBackButton.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModelContact.fetchContacts()
        viewModelVehicle.fetchVehicles()

        val args = arguments
        if (args != null) {

            val title = args.getString("title") ?: ""
            val price = args.getDouble("price")
            val dateOfCreation = args.getString("dateOfCreation") ?: ""

            val contactId = args.getInt("contactId")
            val contactFirstName = args.getString("contactFirstName") ?: ""
            val contactLastName = args.getString("contactLastName") ?: ""

            val vehiclesBundleList = args.getParcelableArrayList<Bundle>("vehicles")
            val selectedVehicleRegistrations = vehiclesBundleList?.mapNotNull { it.getString("registration") } ?: emptyList()

            binding.etOfferAddName.setText(title)
            binding.etOfferPrice.setText(price.toString())

            val dateParts = dateOfCreation.split("-")
            if (dateParts.size == 3) {
                val year = dateParts[0].toIntOrNull() ?: 2023
                val month = (dateParts[1].toIntOrNull() ?: 1) - 1
                val day = dateParts[2].toIntOrNull() ?: 1
                binding.datePicker.updateDate(year, month, day)
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModelContact.contacts.collect { contacts ->
                        val contactNames = contacts.map { "${it.firstName} ${it.lastName}" }
                        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, contactNames)
                        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
                        binding.spinnerContact.adapter = adapter

                        val selectedContactName = "$contactFirstName $contactLastName"
                        val contactIndex = contactNames.indexOf(selectedContactName)
                        if (contactIndex >= 0) {
                            binding.spinnerContact.setSelection(contactIndex)
                        }
                    }
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModelVehicle.vehicles.collect { vehicles ->
                        val vehicleRegistrations = vehicles.map { it.registration }
                        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, vehicleRegistrations)
                        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
                        binding.spinnerVehicle.adapter = adapter
                    }
                }
            }

            selectedVehicleRegistrations.forEach { registration ->
                addVehicleToLayout(registration)
                initialVehicles.add(registration)
            }
        }

        binding.btnAddVehicle.setOnClickListener {
            val selectedVehicle = binding.spinnerVehicle.selectedItem?.toString()
            if (selectedVehicle != null && !selectedVehicles.contains(selectedVehicle) && !initialVehicles.contains(selectedVehicle)) {
                selectedVehicles.add(selectedVehicle)
                addVehicleToLayout(selectedVehicle)
            } else {
                Toast.makeText(requireContext(), "Vozilo već dodano!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnOfferAdd.setOnClickListener {
            val selectedContactIndex = binding.spinnerContact.selectedItemPosition
            val title = binding.etOfferAddName.text.toString()
            val price = binding.etOfferPrice.text.toString().toDoubleOrNull()

            val year = binding.datePicker.year
            val month = binding.datePicker.month
            val dayOfMonth = binding.datePicker.dayOfMonth

            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.set(year, month, dayOfMonth)

            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate = outputFormat.format(selectedCalendar.time)

            val dateOfCreation = formattedDate

            val selectedContactId = viewModelContact.contacts.value.getOrNull(selectedContactIndex)?.id

            val selectedVehicleIds = viewModelVehicle.vehicles.value
                .filter { registration -> selectedVehicles.contains(registration.registration) || initialVehicles.contains(registration.registration) }
                .map { it.id }


            if (selectedContactId != null && selectedVehicleIds.isNotEmpty() && title.isNotBlank() && price != null && dateOfCreation.isNotBlank()) {
                if(args != null){
                    val offer = OfferDto(
                        id = args.getInt("offerId"),
                        title = title,
                        price = price,
                        dateOfCreation = dateOfCreation
                    )
                    viewModelOffer.putOffer(offer, selectedContactId, selectedVehicleIds)
                    Toast.makeText(requireContext(), "Ponuda uspješno dodana!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(com.example.carchive.R.id.action_editOfferFragment_to_offerFragment)

                }
            } else {
                Toast.makeText(requireContext(), "Svi podaci su obavezni!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addVehicleToLayout(vehicle: String) {
        val vehicleBinding = ItemSelectedVehicleBinding.inflate(LayoutInflater.from(requireContext()), binding.llSelectedVehicles, false)

        vehicleBinding.tvVehicleName.text = vehicle

        vehicleBinding.btnRemoveVehicle.setOnClickListener {
            binding.llSelectedVehicles.removeView(vehicleBinding.root)
            selectedVehicles.remove(vehicle)
            initialVehicles.remove(vehicle)
        }

        binding.llSelectedVehicles.addView(vehicleBinding.root)
    }

}