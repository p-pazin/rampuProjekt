package com.example.carchive.fragments

import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.carchive.CarchiveActivity
import com.example.carchive.databinding.VehicleLocationFragmentBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.example.carchive.viewmodels.LocationViewModel
import com.example.carchive.data.network.Result

class VehicleLocationFragment : Fragment(), OnMapReadyCallback {

    private var mapView: MapView? = null
    private var googleMap: GoogleMap? = null
    private var _binding: VehicleLocationFragmentBinding? = null
    private val binding get() = _binding!!

    private val locationViewModel: LocationViewModel by activityViewModels()

    private var vehicleMarker: Marker? = null
    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval: Long = 3000

    private val originalRoute = listOf(
        LatLng(46.3042, 16.3378),
        LatLng(46.2606, 16.3433),
        LatLng(46.2213, 16.3913),
        LatLng(46.1585, 16.3186),
        LatLng(46.0805, 16.1804),
        LatLng(45.9825, 16.0539),
        LatLng(45.8669, 16.0832),
        LatLng(45.8205, 16.0835),
        LatLng(45.8150, 15.9819)
    )

    private val route = generateSmoothRoute(originalRoute)

    private var currentStep = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = VehicleLocationFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.sidebarLogo.drawerToggleButton.setOnClickListener {
            (activity as? CarchiveActivity)?.toggleDrawer()
        }

        mapView = binding.mapView
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)

        locationViewModel.fetchLocations()

        locationViewModel.locations.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Result.Success -> {
                    if (result.data.isNotEmpty()) {
                        val startLocation = LatLng(result.data[0].latitude, result.data[0].longitude)
                        vehicleMarker = googleMap?.addMarker(
                            MarkerOptions().position(startLocation).title("Vehicle Location")
                        )
                        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 10f))
                        simulateVehicleMovement()
                    }
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(), "Error fetching location", Toast.LENGTH_SHORT).show()
                }
            }
        })

        return view
    }

    override fun onMapReady(map: GoogleMap) {
        this.googleMap = map
        googleMap?.uiSettings?.isZoomControlsEnabled = true
        googleMap?.uiSettings?.isCompassEnabled = true
        googleMap?.uiSettings?.isMyLocationButtonEnabled = true
    }

    private fun simulateVehicleMovement() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (currentStep < route.size) {
                    val endPosition = route[currentStep]
                    vehicleMarker?.let { animateMarker(it, it.position, endPosition) }
                    currentStep++
                    handler.postDelayed(this, updateInterval)
                }
            }
        }, updateInterval)
    }


    private fun generateSmoothRoute(route: List<LatLng>): List<LatLng> {
        val smoothRoute = mutableListOf<LatLng>()
        for (i in 0 until route.size - 1) {
            val start = route[i]
            val end = route[i + 1]
            smoothRoute.add(start)

            for (j in 1..3) {
                val fraction = j / 4.0f
                val intermediatePoint = LatLng(
                    (end.latitude - start.latitude) * fraction + start.latitude,
                    (end.longitude - start.longitude) * fraction + start.longitude
                )
                smoothRoute.add(intermediatePoint)
            }
        }
        smoothRoute.add(route.last())
        return smoothRoute
    }


    private fun animateMarker(marker: Marker?, fromPosition: LatLng, toPosition: LatLng) {
        marker ?: return
        val latLngInterpolator = LatLngInterpolator.Linear()
        val animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = updateInterval
            addUpdateListener { animation ->
                val fraction = animation.animatedFraction
                val newPosition = latLngInterpolator.interpolate(fraction, fromPosition, toPosition)
                marker.position = newPosition
            }
        }
        animator.start()
    }


    interface LatLngInterpolator {
        fun interpolate(fraction: Float, a: LatLng, b: LatLng): LatLng

        class Linear : LatLngInterpolator {
            override fun interpolate(fraction: Float, a: LatLng, b: LatLng): LatLng {
                val lat = (b.latitude - a.latitude) * fraction + a.latitude
                val lng = (b.longitude - a.longitude) * fraction + a.longitude
                return LatLng(lat, lng)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        mapView?.onDestroy()
        _binding = null
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }
}
