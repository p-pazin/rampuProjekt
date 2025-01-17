package com.example.carchive.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carchive.data.dto.LocationDto
import com.example.carchive.data.repositories.LocationRepository
import com.example.carchive.data.network.Result
import kotlinx.coroutines.launch

class LocationViewModel : ViewModel() {

    private val locationRepository = LocationRepository()

    private val _locations = MutableLiveData<Result<List<LocationDto>>>()
    val locations: LiveData<Result<List<LocationDto>>> = _locations

    fun fetchLocations() {
        viewModelScope.launch {
            val result = locationRepository.getLocations()
            _locations.value = result
        }
    }
}
