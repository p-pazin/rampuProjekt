package com.example.carchive.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.carchive.data.repositories.AdRepository
import androidx.lifecycle.viewModelScope
import com.example.carchive.data.dto.AdDto
import com.example.carchive.data.dto.AdDtoPost
import com.example.carchive.data.dto.VehicleDto
import com.example.carchive.data.dto.toEntity
import com.example.carchive.data.network.Result
import com.example.carchive.data.repositories.VehicleRepository
import com.example.carchive.entities.Ad
import com.example.carchive.entities.Vehicle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Response

class AdViewModel : ViewModel() {
    private val adRepository = AdRepository()
    private val vehicleRepository = VehicleRepository()

    private val _ads = MutableStateFlow<List<Ad>>(emptyList())
    val ads: StateFlow<List<Ad>> = _ads

    private val _ad = MutableStateFlow<Ad>(Ad.EMPTY)
    val ad: StateFlow<Ad> = _ad

    private val _vehicles = MutableStateFlow<List<Vehicle>>(emptyList())
    val vehicles: StateFlow<List<Vehicle>> = _vehicles

    fun fetchAds() {
        viewModelScope.launch {
            val adsFromRepository = when (val result = adRepository.getAds()) {
                is Result.Success -> result.data
                is Result.Error -> listOf()
            }
            _ads.update { adsFromRepository }
        }
    }

    fun fetchAd(id: Int) {
        viewModelScope.launch {
            val adFromRepository = when (val result = adRepository.getAd(id)) {
                is Result.Success -> result.data
                is Result.Error -> Ad.EMPTY
            }
            _ad.update { adFromRepository }
        }
    }

    fun putAd(ad: AdDto) {
        viewModelScope.launch {
            val adFromRepository = when (val result = adRepository.putAd(ad)) {
                is Result.Success -> result.data
                is Result.Error -> Ad.EMPTY
            }
        }
    }

    fun deleteAd(id: Int) {
        viewModelScope.launch {
            val adFromRepository = when (adRepository.deleteAd(id)) {
                is Result.Success -> true
                is Result.Error -> false
            }
        }
    }

    fun fetchVehicles() {
        viewModelScope.launch {
            val vehiclesFromRepository = when (val result = vehicleRepository.getVehicles()) {
                is Result.Success -> result.data
                is Result.Error -> listOf()
            }
            _vehicles.update { vehiclesFromRepository }
        }
    }

    private val _postResult = MutableLiveData<Result<Response<Unit>>>()
    val postResult: LiveData<Result<Response<Unit>>> = _postResult

    fun postAd(adDtoPost: AdDtoPost, id: Int) {
        viewModelScope.launch {
            try {
                val transformedAd = adDtoPost.toEntity()

                when (val result = adRepository.postAd(transformedAd, id)) {
                    is Result.Success -> {
                        _postResult.postValue(Result.Success(result.data))
                    }
                    is Result.Error -> {
                        _postResult.postValue(Result.Error(result.error))
                    }
                }
            } catch (e: Exception) {
                _postResult.postValue(Result.Error(e))
            }
        }
    }

}

