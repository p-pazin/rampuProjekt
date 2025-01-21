package com.example.carchive.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.carchive.R
import com.example.carchive.data.dto.AdDto
import com.example.carchive.data.dto.AdDtoPost
import com.example.carchive.data.network.Result
import com.example.carchive.databinding.FragmentAddAdBinding
import com.example.carchive.entities.Vehicle
import com.example.carchive.viewmodels.AdViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class AddAdFragment : Fragment() {

    private val adViewModel: AdViewModel by viewModels()
    private var _binding: FragmentAddAdBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddAdBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentDate = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date())
        binding.tvCurrentDate.text = currentDate
        binding.navBackButton.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        adViewModel.vehicles.onEach { vehicles: List<Vehicle> ->
            setupAutoCompleteTextView(vehicles)
        }.launchIn(lifecycleScope)
        adViewModel.fetchVehicles()
        setupPaymentMethodSpinner()

        val btnAddAd: Button = binding.btnAddAd
        val etAdTitle: EditText = binding.etAdTitle
        val etAdDescription: EditText = binding.etAdDescription
        val tvCurrentDate: TextView = binding.tvCurrentDate
        val spnPaymentMethod: Spinner = binding.spnPaymentMethod

        adViewModel.postResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    Toast.makeText(requireContext(), getString(R.string.ad_added), Toast.LENGTH_SHORT).show()
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(), getString(R.string.error_adding_ad), Toast.LENGTH_SHORT).show()
                }
            }
        }
        btnAddAd.setOnClickListener {
            val naslov = etAdTitle.text.toString()
            val opis = etAdDescription.text.toString()
            val nacinPlacanja = spnPaymentMethod.selectedItem.toString()
            val datumObjave = tvCurrentDate.text.toString()
            val voziloId = selectedVehicleId

            if (naslov.isNotBlank() &&
                opis.isNotBlank() &&
                nacinPlacanja.isNotBlank() &&
                datumObjave.isNotBlank() &&
                voziloId != null
            ) {
                val ad = AdDtoPost(
                    title = naslov,
                    description = opis,
                    paymentMethod = nacinPlacanja,
                    dateOfPublishment = datumObjave
                )
                Log.d("AddAdFragment", "ID vozila koje se šalje: $voziloId")

                adViewModel.postAd(ad, voziloId)
            } else {
                Toast.makeText(requireContext(), getString(R.string.info_missing), Toast.LENGTH_SHORT).show()
            }
        }
    }
    private var selectedVehicleId: Int? = null
    var vehicleSelectionMap: Map<String, Int>? = null
    private fun setupAutoCompleteTextView(vehicles: List<Vehicle>) {
        val vehicleNames = vehicles.map { "${it.registration} , ${it.brand} ${it.model}" }
        val vehicleIds = vehicles.map { it.id }
        vehicleSelectionMap = vehicleNames.zip(vehicleIds).toMap()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, vehicleNames)
        binding.actvVehicleList.setAdapter(adapter)
        binding.actvVehicleList.setOnItemClickListener { _, view, _, _ ->
            val selectedText = (view as TextView).text.toString()
            selectedVehicleId = vehicleSelectionMap?.get(selectedText)
        }

    }

    private fun setupPaymentMethodSpinner() {
        val paymentMethods = listOf("Gotovina", "Kartica", "Uplata na račun")
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, paymentMethods)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnPaymentMethod.adapter = spinnerAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
