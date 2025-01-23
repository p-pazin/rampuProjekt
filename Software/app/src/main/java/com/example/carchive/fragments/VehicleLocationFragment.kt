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
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.example.carchive.viewmodels.LocationViewModel
import com.example.carchive.data.network.Result
import java.util.Random

class VehicleLocationFragment : Fragment(), OnMapReadyCallback {

    private var mapView: MapView? = null
    private var googleMap: GoogleMap? = null
    private var _binding: VehicleLocationFragmentBinding? = null
    private val binding get() = _binding!!

    private val locationViewModel: LocationViewModel by activityViewModels()

    private val vehicleMarkers = mutableListOf<Marker>()
    private val random = Random()

    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval: Long = 2000

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
                    val boundsBuilder = LatLngBounds.Builder()
                    result.data.forEach { location ->
                        val latLng = LatLng(location.latitude, location.longitude)
                        val marker = googleMap?.addMarker(
                            MarkerOptions().position(latLng).title("Vehicle Location")
                        )
                        marker?.let { vehicleMarkers.add(it) }
                        boundsBuilder.include(latLng)
                    }

                    if (result.data.isNotEmpty()) {
                        val bounds = boundsBuilder.build()
                        googleMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
                    }

                    simulateVehicleMovement()
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

        val defaultLocation = LatLng(51.5074, -0.1278)
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10f))
    }

    private fun simulateVehicleMovement() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                vehicleMarkers.forEach { marker ->
                    val oldPosition = marker.position
                    val newLat = oldPosition.latitude + (random.nextDouble() - 0.5) * 0.001
                    val newLng = oldPosition.longitude + (random.nextDouble() - 0.5) * 0.001
                    val newPosition = LatLng(newLat, newLng)

                    animateMarker(marker, newPosition)
                }
                handler.postDelayed(this, updateInterval)
            }
        }, updateInterval)
    }

    private fun animateMarker(marker: Marker, toPosition: LatLng) {
        val startPosition = marker.position
        val latLngInterpolator = LatLngInterpolator.Linear()

        val animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = updateInterval
            addUpdateListener { animation ->
                val fraction = animation.animatedFraction
                val newPosition = latLngInterpolator.interpolate(fraction, startPosition, toPosition)
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
