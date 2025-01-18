package com.example.carchive.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carchive.data.dto.OfferDto
import com.example.carchive.data.dto.VehicleDto
import com.example.carchive.data.network.Result
import com.example.carchive.data.repositories.OfferRepository
import com.example.carchive.entities.Vehicle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OfferViewModel : ViewModel() {
    private val offerRepository = OfferRepository()
    private val _offers = MutableStateFlow<List<OfferDto>>(listOf())
    val offers = _offers.asStateFlow()

    private val _vehicles = MutableStateFlow<List<Vehicle>>(listOf())
    val vehicles = _vehicles.asStateFlow()

    fun fetchOffers() {
        viewModelScope.launch {
            val offersFromRepository = when (val result = offerRepository.getOffers()) {
                is Result.Success -> result.data
                is Result.Error -> {
                    Log.e("OfferVM", "Greška prilikom dohvaćanja ponuda: ${result.error}")
                    listOf()
                }
            }
            Log.d("com.example.carchive.viewmodels.OfferViewModel", "Dohvaćene ponude: $offersFromRepository")
            _offers.update { offersFromRepository }
        }
    }

    fun fetchVehiclesForOffers(offerId: Int) {
        viewModelScope.launch {
            val vehiclesFromRepository = when (val result = offerRepository.getVehiclesForOffer(offerId)) {
                is Result.Success -> result.data
                is Result.Error -> {
                    Log.e("OfferVM", "Greška prilikom dohvaćanja vozila za ponudu: ${offerId} ${result.error}")
                    listOf()
                }
            }
            Log.d("com.example.carchive.viewmodels.OfferViewModel", "Dohvaćene ponude: ${vehiclesFromRepository}")
            _vehicles.update { vehiclesFromRepository }
        }
    }
}
