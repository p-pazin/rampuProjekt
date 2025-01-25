package com.example.carchive.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.carchive.data.dto.ContactDto
import com.example.carchive.data.dto.ReservationDetails
import com.example.carchive.data.dto.ReservationDto
import com.example.carchive.data.dto.VehicleDto
import com.example.carchive.data.dto.toDto
import com.example.carchive.databinding.FragmentEditReservationBinding
import com.example.carchive.viewmodels.ContractsViewModel
import com.example.carchive.viewmodels.ReservationViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

class EditReservationFragment : Fragment() {

    private var _binding: FragmentEditReservationBinding? = null
    private val binding get() = _binding!!
    private val reservationViewModel: ReservationViewModel by viewModels()
    private val contractViewMode: ContractsViewModel by viewModels()

    private var vehicleList: List<VehicleDto> = emptyList()
    private var contactList: List<ContactDto> = emptyList()
    private var selectedContactId: Int = 0
    private var selectedVehicleId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditReservationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.navBackButton.backButton.setOnClickListener(){
            findNavController().popBackStack()
        }
        binding.spnContractAddContact.isEnabled = false

        val reservationId = arguments?.getInt("reservationId") ?: return
        val vehicleId = arguments?.getInt("vehicleId") ?: return
        val contactId = arguments?.getInt("contactId") ?: return

        contractViewMode.fetchVehiclesRent()
        contractViewMode.fetchContacts()

        lifecycleScope.launch {
            contractViewMode.vehicles.collect { vehicles ->
                vehicles?.let {
                    vehicleList = it.map { it.toDto() }
                    initVehicleSpinner(vehicleId)
                }
            }
        }

        lifecycleScope.launch {
            contractViewMode.contacts.collect { contacts ->
                contacts?.let {
                    contactList = it.map { it.toDto() }
                    initContactSpinner(contactId)
                }
            }
        }

        reservationViewModel.fetchReservationDetails(reservationId)
        reservationViewModel.reservationDetails.observe(viewLifecycleOwner) { details ->
            details?.let {
                populateFields(it)
            }
        }

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

        setupListeners(reservationId)
    }

    private fun initVehicleSpinner(id: Int) {
        if (vehicleList.isEmpty()) return

        val vehicleAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            vehicleList.map { "${it.registration}, ${it.brand} ${it.model}" }
        )
        vehicleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnContractAddVehicle.adapter = vehicleAdapter

        val selectedVehicle = vehicleList.find { it.id == id }
        selectedVehicle?.let {
            val position = vehicleList.indexOf(it)
            binding.spnContractAddVehicle.setSelection(position)
        }
    }

    private fun initContactSpinner(id: Int) {
        if (contactList.isEmpty()) return

        val contactAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            contactList.map { "${it.firstName} ${it.lastName}" }
        )
        contactAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnContractAddContact.adapter = contactAdapter

        // Set default selection if selectedContactId is set correctly
        val selectedContact = contactList.find { it.id == id }
        selectedContact?.let {
            val position = contactList.indexOf(it)
            binding.spnContractAddContact.setSelection(position)
        }
    }


    private fun populateFields(details: ReservationDetails) {
        binding.etContractAddPrice.setText(details.reservation.price.toString())
        binding.etContractAddMaxMileage.setText(details.reservation.maxMileage.toString())
        binding.etContractAddDateOfCreation.setText(details.reservation.dateOfCreation)
        binding.btnContractAddStartDate.text = details.reservation.startDate
        binding.btnContractAddEndDate.text = details.reservation.endDate

        selectedVehicleId = details.reservation.vehicleId
        selectedContactId = details.reservation.contactId
    }

    private fun setupListeners(reservationId: Int) {
        binding.btnContractAdd.setOnClickListener {
            val price = binding.etContractAddPrice.text.toString().toIntOrNull()
            val maxMileage = binding.etContractAddMaxMileage.text.toString().toIntOrNull()
            val startDate = binding.btnContractAddStartDate.text.toString()
            val endDate = binding.btnContractAddEndDate.text.toString()
            val dateOfCreation = binding.etContractAddDateOfCreation.text.toString()

            if (price != null && maxMileage != null && startDate.isNotEmpty() && endDate.isNotEmpty()) {
                val updatedReservation = ReservationDto(
                    id = reservationId,
                    price = price,
                    startDate = startDate,
                    endDate = endDate,
                    dateOfCreation = dateOfCreation,
                    maxMileage = maxMileage,
                    vehicleId = selectedVehicleId,
                    contactId = selectedContactId,
                    state = 1
                )
                reservationViewModel.changeReservation(updatedReservation)
            } else {
                Toast.makeText(requireContext(), "Molimo vas da ispunite sva polja.", Toast.LENGTH_SHORT).show()
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

