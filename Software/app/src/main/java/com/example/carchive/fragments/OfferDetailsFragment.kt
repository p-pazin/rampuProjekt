package com.example.carchive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.carchive.adapters.CarAdapter
import com.example.carchive.data.network.Result
import com.example.carchive.databinding.OfferDetailsBinding
import com.example.carchive.entities.Contact
import com.example.carchive.entities.Vehicle
import com.example.carchive.viewmodels.ContactsViewModel
import com.example.carchive.viewmodels.OfferViewModel
import com.example.carchive.viewmodels.VehicleCatalogViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class OfferDetailsFragment : Fragment() {
    private var _binding: OfferDetailsBinding? = null
    private val binding get() = _binding!!
    private val offerViewModel: OfferViewModel by viewModels()
    private val contactViewModel: ContactsViewModel by viewModels()
    private val vehicleCatalogViewModel: VehicleCatalogViewModel by viewModels()
    private lateinit var carAdapter: CarAdapter
    private var offerId: Int = 0



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = OfferDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.navBackButton.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        arguments?.let { bundle ->
            offerId = bundle.getInt("id", 0)

            binding.tvOfferId.text = "ID: $offerId"
            binding.offerTitle.text = "${bundle.getString("title", "Naslov")}"
            binding.offerPrice.text = "Cijena: ${bundle.getDouble("price", 0.0).toString()} €"
            binding.offerDate.text = "${bundle.getString("dateOfCreation", "2025-12-12")}"
        }

        setupRecyclerView()
        observeViewModel()
        contactViewModel.fetchContactForOffer(offerId)
        offerViewModel.fetchVehiclesForOffers(offerId)

        binding.btnDeleteOffer.setOnClickListener {
            offerViewModel.deleteOffer(offerId)
        }

        binding.btnEditOffer.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val contact = contactViewModel.contact.value
                val vehicles = offerViewModel.vehicles.value

                if (contact != null) {
                    val bundle = Bundle().apply {
                        putInt("offerId", offerId)
                        putString("title", binding.offerTitle.text.toString())
                        putDouble(
                            "price",
                            binding.offerPrice.text.toString()
                                .removePrefix("Cijena: ")
                                .removeSuffix(" €")
                                .toDouble()
                        )
                        putString(
                            "dateOfCreation",
                            binding.offerDate.text.toString()
                        )

                        putInt("contactId", contact.id)
                        putString("contactFirstName", contact.firstName)
                        putString("contactLastName", contact.lastName)

                        val vehiclesBundleList = ArrayList<Bundle>()
                        vehicles.forEach { vehicle ->
                            val vehicleBundle = Bundle().apply {
                                putInt("vehicleId", vehicle.id)
                                putString("registration", vehicle.registration)
                            }
                            vehiclesBundleList.add(vehicleBundle)
                        }
                        putParcelableArrayList("vehicles", vehiclesBundleList)
                    }

                    findNavController().navigate(R.id.action_offerDetailsFragment_to_editOfferFragment, bundle)
                } else {
                    Toast.makeText(requireContext(), "Podaci nisu dostupni!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        carAdapter = CarAdapter(
            vehicles = emptyList(),
            onDetailsClick = { view, vehicle ->
            },
            onCarCardClick = { _, vehicle ->
                val bundle = Bundle()

                bundle.putInt("id", vehicle.id)
                bundle.putString("marka", vehicle.brand)
                bundle.putString("model", vehicle.model)
                bundle.putString("type", vehicle.type)
                bundle.putString("productionYear", vehicle.productionYear)
                bundle.putString("registration", vehicle.registration)
                bundle.putInt("kilometers", vehicle.kilometers)
                bundle.putString("location", vehicle.location)
                bundle.putString("motor", vehicle.motor)
                bundle.putDouble("enginePower", vehicle.enginePower)
                bundle.putString("gearbox", vehicle.gearbox.externalName)
                bundle.putInt("rentSell", vehicle.rentSell)
                bundle.putDouble("price", vehicle.price)
                bundle.putString("imageCar", vehicle.imageCar)
                bundle.putDouble("cubicCapacity", vehicle.cubicCapacity)
                bundle.putString("color", vehicle.color)
                bundle.putString("driveType", vehicle.driveType)
                bundle.putString("condition", vehicle.condition)
                bundle.putString("registeredTo", vehicle.registeredTo)
                findNavController().navigate(R.id.action_offerDetailsFragment_to_vehicleDetailsFragment, bundle)
            },  { vehicleId, callback ->
                viewLifecycleOwner.lifecycleScope.launch {
                    when (val result = vehicleCatalogViewModel.getVehiclePhotosCatalog(vehicleId)) {
                        is Result.Success -> {
                            val firstPhotoUrl = result.data.firstOrNull()?.link
                            callback(firstPhotoUrl)
                        }
                        is Result.Error -> {
                            callback(null)
                        }
                    }
                }
            }
        )
        binding.recyclerVehicles.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = carAdapter
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            offerViewModel.vehicles.collectLatest { vehicles ->
                updateRecyclerView(vehicles)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            contactViewModel.contact.collectLatest { contact ->
                if (contact != null) {
                    updateContactDetails(contact)
                }
            }
        }

        offerViewModel.deleteResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    Toast.makeText(requireContext(), "Ponuda uspješno obrisana!", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
                is Result.Error -> {
                    val errorMessage = result.error.message ?: "Unknown error occurred"
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                }
            }
        }

    }


    private fun updateRecyclerView(vehicles: List<Vehicle>) {
        carAdapter.updateItems(vehicles)
    }

    private fun updateContactDetails(contact: Contact) {
        binding.contactName.text = "${contact.firstName} ${contact.lastName}"
        binding.contactPin.text = "OIB: ${contact.pin}"
        binding.contactAddress.text = "Adresa: ${contact.address}, ${contact.city}, ${contact.country}"
        binding.contactTelephone.text = "Telefon: ${contact.telephoneNumber}"
        binding.contactMobile.text = "Mobitel: ${contact.mobileNumber}"
        binding.contactEmail.text = "Email: ${contact.email}"
        binding.contactDescription.text = "Opis: ${contact.description}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
