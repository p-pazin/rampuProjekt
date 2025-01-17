package com.example.carchive.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.carchive.R
import com.example.carchive.data.network.Result
import com.example.carchive.databinding.FragmentContactDetailsBinding
import com.example.carchive.entities.Contact
import com.example.carchive.viewmodels.ContactsViewModel

class ContactDetailsFragment : Fragment() {

    private val viewModel : ContactsViewModel by viewModels()
    private lateinit var tvName : TextView
    private lateinit var ivState : ImageView
    private lateinit var tvState: TextView
    private lateinit var tvCountry : TextView
    private lateinit var tvCity : TextView
    private lateinit var tvAddress : TextView
    private lateinit var tvEmail : TextView
    private lateinit var tvPhoneNumber : TextView
    private lateinit var tvMobileNumber : TextView
    private lateinit var tvDescription : TextView
    private lateinit var tvPin : TextView
    private var _binding: FragmentContactDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val contact = arguments?.getSerializable("contact_key") as? Contact

        _binding = FragmentContactDetailsBinding.inflate(inflater, container, false)
        binding.navBackButton.backButton.setOnClickListener(){
            findNavController().popBackStack()
        }

        binding.btnContactDetailsEdit.setOnClickListener(){
            val bundle = Bundle().apply {
                putSerializable("contact_key", contact)
            }
            findNavController().navigate(R.id.action_contactsDetailsFragment_to_contactUpdateFragment, bundle)
        }


        binding.btnContactDetailsDelete.setOnClickListener() {
            viewModel.deleteContact(contact!!.id)

            viewModel.deleteResult.observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Result.Success -> {
                        Toast.makeText(requireContext(), getString(R.string.kontaktObrisan), Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_contactsDetailsFragment_to_contactsFragment)
                    }
                    is Result.Error -> {
                        Toast.makeText(requireContext(), getString(R.string.greskaKodBrisanjaKontakta), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        tvName = binding.tvContactDetailsName
        ivState = binding.ivContactDetailsState
        tvState = binding.tvContactDetailsState
        tvCountry = binding.tvContactDetailsCountry
        tvCity = binding.tvContactDetailsCity
        tvAddress =binding.tvContactDetailsAddress
        tvEmail = binding.tvContactDetailsEmail
        tvPin = binding.tvContactDetailsPin
        tvPhoneNumber = binding.tvContactDetailsPhone
        tvMobileNumber = binding.tvContactDetailsMobile
        tvDescription = binding.tvContactDetailsDescription


        if (contact != null) {
            if(contact.state == 1) {
                ivState.setImageResource(R.drawable.ic_aktivan_kontakt)
                tvState.text = "Aktivan kontakt"
            }
            else {
                ivState.setImageResource(R.drawable.ic_neaktivan_kontakt)
                tvState.text = "Neaktivan kontakt"
            }
        }

        if (contact != null) {
            tvName.text = contact.firstName + " " + contact.lastName
        }
        if (contact != null) {
            tvCountry.text = contact.country
        }
        if (contact != null) {
            tvCity.text = contact.city
        }
        if (contact != null) {
            tvAddress.text = contact.address
        }
        if (contact != null) {
            tvEmail.text = "E-adresa: " + contact.email
        }
        if (contact != null) {
            tvPin.text = "OIB: " + contact.pin
        }
        if (contact != null) {
            tvPhoneNumber.text = "Telefon: " + contact.telephoneNumber
        }
        if (contact != null) {
            tvMobileNumber.text = "Mobilni telefon: " + contact.mobileNumber
        }
        if (contact != null) {
            tvDescription.text = contact.description
        }

        return binding.root
    }
}