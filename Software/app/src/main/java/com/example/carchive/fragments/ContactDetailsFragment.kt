package com.example.carchive.fragments

import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.carchive.R
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_contact_details, container, false)

        tvName = view.findViewById(R.id.tv_contact_details_name)
        ivActivity = view.findViewById(R.id.img_contact_details_activity)
        tvActivity = view.findViewById(R.id.tv_contact_details_activity)
        ivState = view.findViewById(R.id.img_contact_details_state)
        tvState = view.findViewById(R.id.tv_contact_details_state)
        ivOfferSent = view.findViewById(R.id.img_contact_details_offer_sent)
        tvOfferSent = view.findViewById(R.id.tv_contact_details_offer_sent)
        tvCountry = view.findViewById(R.id.tv_contact_details_country)
        tvCity = view.findViewById(R.id.tv_contact_details_city)
        tvAddress = view.findViewById(R.id.tv_contact_details_address)
        tvEmail = view.findViewById(R.id.tv_contact_details_email)
        tvPin = view.findViewById(R.id.tv_contact_details_pin)
        tvPhoneNumber = view.findViewById(R.id.tv_contact_details_phone)
        tvMobileNumber = view.findViewById(R.id.tv_contact_details_mobile)
        tvDescription = view.findViewById(R.id.tv_contact_details_description)

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

        return view
    }
}