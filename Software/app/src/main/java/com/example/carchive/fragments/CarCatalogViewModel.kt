package com.example.carchive.fragments

import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carchive.data.network.Result
import com.example.carchive.data.repositories.VehicleRepository
import com.example.carchive.entities.Vehicle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CarCatalogViewModel : ViewModel() {
    private val vehicleRepository = VehicleRepository()
    private val _vehicles = MutableStateFlow<List<Vehicle>>(listOf())
    val vehicles = _vehicles.asStateFlow()

    fun fetchVehicles() {
        viewModelScope.launch {
            val vehiclesFromRepository = when (val result = vehicleRepository.getVehicles()) {
                is Result.Success -> result.data
                is Result.Error -> listOf()
            }
            _vehicles.update { vehiclesFromRepository }
        }
    }
}