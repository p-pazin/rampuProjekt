package com.example.carchive.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.carchive.R
import com.example.carchive.databinding.FragmentContactAddBinding
import com.example.carchive.databinding.FragmentContactDetailsBinding
import com.example.carchive.databinding.FragmentContactsBinding
import com.example.carchive.entities.Contact
import com.example.carchive.helpers.MockDataLoader

class ContactDetailsFragment : Fragment() {

    private lateinit var tvName : TextView
    private lateinit var ivActivity : ImageView
    private lateinit var tvActivity : TextView
    private lateinit var ivState : ImageView
    private lateinit var tvState: TextView
    private lateinit var ivOfferSent : ImageView
    private lateinit var tvOfferSent : TextView
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
            findNavController().popBackStack()
        }

        tvName = binding.tvContactDetailsName
        ivActivity = binding.ivContactDetailsActivity
        tvActivity = binding.tvContactDetailsActivity
        ivState = binding.ivContactDetailsState
        tvState = binding.tvContactDetailsState
        ivOfferSent = binding.ivContactDetailsOfferSent
        tvOfferSent = binding.tvContactDetailsOfferSent
        tvCountry = binding.tvContactDetailsCountry
        tvCity = binding.tvContactDetailsCity
        tvAddress =binding.tvContactDetailsAddress
        tvEmail = binding.tvContactDetailsEmail
        tvPin = binding.tvContactDetailsPin
        tvPhoneNumber = binding.tvContactDetailsPhone
        tvMobileNumber = binding.tvContactDetailsMobile
        tvDescription = binding.tvContactDetailsDescription

        if (contact != null) {
            if(contact.offerSent) {
                ivOfferSent.setImageResource(R.drawable.ic_check)
                tvOfferSent.text = "Ponuda poslana"
            }
            else {
                ivOfferSent.setImageResource(R.drawable.ic_x)
                tvOfferSent.text = "Ponuda nije poslana"
            }
        }

        if (contact != null) {
            if(contact.state == "Aktivni kontakt") {
                ivState.setImageResource(R.drawable.ic_aktivan_kontakt)
                tvState.text = "Aktivni kontakt"
            }
            else {
                ivState.setImageResource(R.drawable.ic_neaktivan_kontakt)
                tvState.text = "Neaktivni kontakt"
            }
        }

        if (contact != null) {
            tvActivity.text = contact.activity
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
            tvEmail.text = "E-adresa: " + contact.emailAddress
        }
        if (contact != null) {
            tvPin.text = "OIB: " + contact.pin
        }
        if (contact != null) {
            tvPhoneNumber.text = "Telefon: " + contact.phoneNumber
        }
        if (contact != null) {
            tvMobileNumber.text = "Mobilni telefon: " + contact.mobilePhoneNumber
        }
        if (contact != null) {
            tvDescription.text = contact.description
        }

        return binding.root
    }
}