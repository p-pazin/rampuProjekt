package com.example.carchive.fragments

import android.os.Bundle
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
        _binding = FragmentContactDetailsBinding.inflate(inflater, container, false)
        binding.navBackButton.backButton.setOnClickListener(){
            findNavController().popBackStack()
        }

        binding.btnContactDetailsEdit.setOnClickListener(){
            findNavController().navigate(R.id.action_contactsDetailsFragment_to_contactUpdateFragment)
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

        val mockUser = MockDataLoader.getMockContacts()[0]

        if(mockUser.offerSent) {
            ivOfferSent.setImageResource(R.drawable.ic_check)
            tvOfferSent.text = "Ponuda poslana"
        }
        else {
            ivOfferSent.setImageResource(R.drawable.ic_x)
            tvOfferSent.text = "Ponuda nije poslana"
        }

        if(mockUser.state == "Aktivni kontakt") {
            ivState.setImageResource(R.drawable.ic_aktivan_kontakt)
            tvState.text = "Aktivni kontakt"
        }
        else {
            ivState.setImageResource(R.drawable.ic_neaktivan_kontakt)
            tvState.text = "Neaktivni kontakt"
        }

        tvActivity.text = mockUser.activity
        tvName.text = mockUser.firstName + " " + mockUser.lastName
        tvCountry.text = mockUser.country
        tvCity.text = mockUser.city
        tvAddress.text = mockUser.address
        tvEmail.text = "E-adresa: " + mockUser.emailAddress
        tvPin.text = "OIB: " + mockUser.pin
        tvPhoneNumber.text = "Telefon: " + mockUser.phoneNumber
        tvMobileNumber.text = "Mobilni telefon: " + mockUser.mobilePhoneNumber
        tvDescription.text = mockUser.description

        return binding.root
    }
}