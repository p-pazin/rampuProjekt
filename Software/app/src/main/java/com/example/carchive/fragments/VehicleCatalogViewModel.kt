package com.example.carchive.fragments

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carchive.data.dto.VehicleDto
import com.example.carchive.data.dto.VehicleDtoPost
import com.example.carchive.data.network.Result
import com.example.carchive.data.repositories.VehicleRepository
import com.example.carchive.entities.Vehicle
import com.example.carchive.util.safeResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.http.HTTP

class VehicleCatalogViewModel : ViewModel() {
    private val vehicleRepository = VehicleRepository()
    private val _vehicles = MutableStateFlow<List<Vehicle>>(listOf())
    val vehicles = _vehicles.asStateFlow()

    private val _postResult = MutableLiveData<Result<Response<Unit>>>()
    val postResult: LiveData<Result<Response<Unit>>> = _postResult

    private val _putResult = MutableLiveData<Result<Response<Unit>>>()
    val putResult: LiveData<Result<Response<Unit>>> = _putResult

    private val _deleteResult = MutableLiveData<Result<Response<Unit>>>()
    val deleteResult: LiveData<Result<Response<Unit>>> = _deleteResult

    fun fetchVehicles() {
        viewModelScope.launch {
            val vehiclesFromRepository = when (val result = vehicleRepository.getVehicles()) {
                is Result.Success -> result.data
                is Result.Error -> {
                    Log.e("VehicleCatalogViewModel", "Greška prilikom dohvaćanja vozila: ${result.error}")
                    listOf()
                }
            }
            Log.d("VehicleCatalogViewModel", "Dohvaćena vozila: $vehiclesFromRepository")
            _vehicles.update { vehiclesFromRepository }
        }
    }


    fun postVehicle(vehicle: VehicleDtoPost) {
        viewModelScope.launch {
            try {
                when (val result = vehicleRepository.postVehicle(vehicle)) {
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

    fun putVehicle(id: Int, vehicle: VehicleDto){
        viewModelScope.launch {
            try {
                when (val result = vehicleRepository.putVehicle(id, vehicle)) {
                    is Result.Success -> {
                        _putResult.postValue(Result.Success(result.data))
                    }
                    is Result.Error -> {
                        _putResult.postValue(Result.Error(result.error))
                    }
                }
            } catch (e: Exception) {
                _putResult.postValue(Result.Error(e))
            }
        }
    }

    fun deleteVehicle(id: Int){
        viewModelScope.launch {
            try {
                when (val result = vehicleRepository.deleteVehicle(id)) {
                    is Result.Success -> {
                        _deleteResult.postValue(Result.Success(result.data))
                    }
                    is Result.Error -> {
                        _deleteResult.postValue(Result.Error(result.error))
                    }
                }
            } catch (e: Exception) {
                _deleteResult.postValue(Result.Error(e))
            }
        }
    }
}