package com.example.carchive.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.carchive.R
import com.example.carchive.adapters.ReservationAdapter
import com.example.carchive.data.dto.ReservationDto
import com.example.carchive.data.network.Result
import com.example.carchive.databinding.FragmentAddReservationBinding
import com.example.carchive.entities.Contact
import com.example.carchive.entities.Vehicle
import com.example.carchive.viewmodels.ContractsViewModel
import com.example.carchive.viewmodels.ReservationViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddReservationFragment : Fragment() {
    private var _binding: FragmentAddReservationBinding? = null
    private val binding get() = _binding!!
    private val reservationViewModel: ReservationViewModel by viewModels()
    private val contractsViewModel : ContractsViewModel by viewModels()
    private lateinit var vehicleSpinner: Spinner
    private lateinit var contactSpinner: Spinner
    var selectedContactId: Int? = null
    var selectedVehicleId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddReservationBinding.inflate(inflater, container, false)
        vehicleSpinner = binding.spnContractAddVehicle
        contactSpinner = binding.spnContractAddContact
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.navBackButton.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        binding.etContractAddDateOfCreation.setText(currentDate)

        binding.btnContractAddStartDate.setOnClickListener {
            showDatePicker { selectedDate ->
                binding.btnContractAddStartDate.text = selectedDate
            }
        }

        binding.btnContractAddEndDate.setOnClickListener {
            showDatePicker { selectedDate ->
                binding.btnContractAddEndDate.text = selectedDate
            }
        }

        reservationViewModel.newReservation.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    Toast.makeText(context, "Rezervacija uspješno dodana!", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
                is Result.Error -> {
                    Toast.makeText(context, "Greška: ${result.error.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        contractsViewModel.fetchVehicles()
        contractsViewModel.fetchContacts()

        lifecycleScope.launch {
            contractsViewModel.vehicles.collect { vehicles ->
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
            contractsViewModel.contacts.collect { contacts ->
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


        binding.btnContractAdd.setOnClickListener {
            val price = binding.etContractAddPrice.text.toString().toIntOrNull()
            val maxMileage = binding.etContractAddMaxMileage.text.toString().toIntOrNull()
            val startDate = binding.btnContractAddStartDate.text.toString()
            val endDate = binding.btnContractAddEndDate.text.toString()
            val dateOfCreation = binding.etContractAddDateOfCreation.text.toString()
            val selectedVehicleId = binding.spnContractAddVehicle.selectedItemId.toInt()
            val selectedContactId = binding.spnContractAddContact.selectedItemId.toInt()

            if (price != null && maxMileage != null && startDate.isNotEmpty() && endDate.isNotEmpty()) {
                val newReservation = ReservationDto(
                    id = 0,
                    state = 0,
                    price = price,
                    startDate = startDate,
                    endDate = endDate,
                    dateOfCreation = dateOfCreation,
                    maxMileage = maxMileage,
                    vehicleId = selectedVehicleId,
                    contactId = selectedContactId
                )

                reservationViewModel.addNewReservation(newReservation)
            } else {
                Toast.makeText(context, "Molimo ispunite sva polja.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
            onDateSelected(formattedDate)
        }, year, month, day).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}