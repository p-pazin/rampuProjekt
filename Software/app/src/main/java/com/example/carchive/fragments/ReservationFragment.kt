package com.example.carchive.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carchive.CarchiveActivity
import com.example.carchive.R
import com.example.carchive.adapters.ReservationAdapter
import com.example.carchive.databinding.FragmentReservationsBinding
import com.example.carchive.viewmodels.ReservationViewModel
import kotlinx.coroutines.launch

class ReservationFragment : Fragment() {
    private var _binding: FragmentReservationsBinding? = null
    private val binding get() = _binding!!
    private val reservationViewModel: ReservationViewModel by viewModels()
    private lateinit var reservationAdapter: ReservationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReservationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reservationAdapter = ReservationAdapter(emptyList()) { reservationId ->
            val bundle = Bundle().apply {
                putInt("reservationId", reservationId)
            }
            findNavController().navigate(R.id.action_reservationFragment_to_reservationInfoFragment, bundle)
        }


        binding.recyclerViewAds.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = reservationAdapter
        }

        reservationViewModel.reservationsWithDetails.observe(viewLifecycleOwner) { reservations ->
            reservationAdapter.updateReservations(reservations)
            binding.tvNoReservations.visibility = if (reservations.isEmpty()) View.VISIBLE else View.GONE
        }

        reservationViewModel.fetchReservations()

        binding.sidebarLogo.drawerToggleButton.setOnClickListener {
            (activity as? CarchiveActivity)?.toggleDrawer()
        }

        binding.sidebarLogo.btnDodaj.setOnClickListener {
            findNavController().navigate(R.id.action_reservationFragment_to_addReservationFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
