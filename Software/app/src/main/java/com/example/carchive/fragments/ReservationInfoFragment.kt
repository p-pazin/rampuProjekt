package com.example.carchive.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.carchive.R
import com.example.carchive.databinding.FragmentReservationInfoBinding
import com.example.carchive.viewmodels.ReservationViewModel

class ReservationInfoFragment : Fragment() {

    private var _binding: FragmentReservationInfoBinding? = null
    private val binding get() = _binding!!
    private val reservationViewModel: ReservationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReservationInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val reservationId = arguments?.getInt("reservationId") ?: return

        reservationViewModel.fetchReservationDetails(reservationId)

        reservationViewModel.reservationDetails.observe(viewLifecycleOwner) { details ->
            details?.let { reservation ->
                binding.tvReservationDateOfCreation.text = "Datum: \n${reservation.reservation.dateOfCreation}"
                binding.tvReservationStartDate.text ="Od: \n${reservation.reservation.startDate}"
                binding.tvReservationEndDate.text ="Do: \n${reservation.reservation.endDate}"
                binding.tvReservationMaxMileage.text ="Max kilometraza: \n${reservation.reservation.maxMileage}"
                binding.tvReservationPrice.text ="Cijena po danu: \n${reservation.reservation.price}"

                binding.tvReservationContactName.text =
                    "Ime i prezime: \n${reservation.contact.firstName} ${reservation.contact.lastName}"
                binding.tvReservationContactEmail.text = "Email: \n${reservation.contact.email}"
                binding.tvReservationContactPhone.text = "Mobilni broj: \n${reservation.contact.mobileNumber}"

                binding.tvReservationVehicleId.text = "ID vozila: \n${reservation.vehicle.id}"
                binding.tvReservationVehicleRegistration.text = "Registracija: \n${reservation.vehicle.registration}"
                binding.tvReservationVehicle.text = "Vozilo: \n${reservation.vehicle.model} ${reservation.vehicle.brand}"
            }
        }

        binding.navBackButton.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnReservationEdit.setOnClickListener {
            reservationViewModel.reservationDetails.value?.let { reservationDetails ->
                val bundle = Bundle().apply {
                    putInt("reservationId", reservationDetails.reservation.id)
                    putInt("vehicleId", reservationDetails.vehicle.id)
                    putInt("contactId", reservationDetails.contact.id)
                }
                findNavController().navigate(
                    R.id.action_reservationInfoFragment_to_editReservationFragment,
                    bundle
                )
            }
        }
        binding.btnReservationDelete.setOnClickListener {
            reservationViewModel.reservationDetails.value?.let { reservationDetails ->
                reservationViewModel.deleteReservation(reservationDetails.reservation.id)
                findNavController().previousBackStackEntry?.savedStateHandle?.set("refreshList", true)
                findNavController().popBackStack()
            }
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
