package com.example.carchive.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.carchive.R
import com.example.carchive.data.dto.AdDto
import com.example.carchive.data.dto.AdDtoPost
import com.example.carchive.databinding.FragmentEditAdBinding
import com.example.carchive.viewmodels.AdViewModel

class EditAdFragment : Fragment() {

    private val adViewModel: AdViewModel by activityViewModels()
    private var _binding: FragmentEditAdBinding? = null
    private val binding get() = _binding!!
    private var adId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditAdBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.navBackButton.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        adId = arguments?.getInt("adId")
        val adTitle = arguments?.getString("adTitle")
        val adDescription = arguments?.getString("adDescription")
        val adPaymentMethod = arguments?.getString("adPaymentMethod")
        val adBrand = arguments?.getString("adBrand")
        val adModel = arguments?.getString("adModel")
        val adDate = arguments?.getString("adDate")

        binding.etAdTitle.setText(adTitle)
        binding.etAdDescription.setText(adDescription)
        adPaymentMethod?.let { getPaymentMethodIndex(it) }
            ?.let { binding.spnPaymentMethod.setSelection(it) }

        binding.tvCurrentDate.text = adDate
        binding.actvVehicleList.setText("$adBrand $adModel")

        binding.tvCurrentDate.isEnabled = false
        binding.actvVehicleList.isEnabled = false

        setupPaymentMethodSpinner()

        binding.btnEditAd.setOnClickListener {
            saveAdChanges()
        }
    }

    private fun getPaymentMethodIndex(paymentMethod: String): Int {
        val paymentMethods = listOf("Gotovina", "Kartica", "Uplata na račun")
        return paymentMethods.indexOf(paymentMethod).takeIf { it >= 0 } ?: 0
    }

    private fun saveAdChanges() {
        val title = binding.etAdTitle.text.toString()
        val description = binding.etAdDescription.text.toString()
        val paymentMethod = binding.spnPaymentMethod.selectedItem.toString()
        println("AAdsadad, $adId")
        if (title.isNotBlank() && description.isNotBlank() && paymentMethod.isNotBlank() && adId != null) {
            val updatedAd = AdDto(
                id = adId!!,
                title = title,
                description = description,
                paymentMethod = paymentMethod,
                dateOfPublishment = binding.tvCurrentDate.text.toString(), // Not changed
                brand = "",
                model = "",
                links = emptyList()
            )

            adViewModel.putAd(updatedAd)
            Toast.makeText(requireContext(), getString(R.string.ad_edited), Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        } else {
            Toast.makeText(requireContext(), getString(R.string.info_missing), Toast.LENGTH_SHORT).show()
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


