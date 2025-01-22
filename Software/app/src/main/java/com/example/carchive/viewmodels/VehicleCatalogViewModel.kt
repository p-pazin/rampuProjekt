package com.example.carchive.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carchive.data.dto.UploadResponse
import com.example.carchive.data.dto.VehicleDto
import com.example.carchive.data.dto.VehicleDtoPost
import com.example.carchive.data.network.Result
import com.example.carchive.data.repositories.VehicleRepository
import com.example.carchive.entities.Vehicle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.File

class VehicleCatalogViewModel : ViewModel() {
    private val vehicleRepository = VehicleRepository()
    private val _vehicles = MutableStateFlow<List<Vehicle>>(listOf())
    val vehicles = _vehicles.asStateFlow()

    private val _postResult = MutableLiveData<Result<Response<Unit>>>()
    val postResult: MutableLiveData<Result<Response<Unit>>> = _postResult

    private val _putResult = MutableLiveData<Result<Response<Unit>>>()
    val putResult: LiveData<Result<Response<Unit>>> = _putResult

    private val _deleteResult = MutableLiveData<Result<Response<Unit>>>()
    val deleteResult: LiveData<Result<Response<Unit>>> = _deleteResult

    private val _vehicleId = MutableLiveData<Int?>()
    val vehicleId: LiveData<Int?> get() = _vehicleId

    private val _uploadResponse = MutableLiveData<Result<Response<String>>>()
    val uploadResponse: LiveData<Result<Response<String>>> get() = _uploadResponse

    private val _connectedResponse = MutableLiveData<Result<Response<Unit>>>()
    val connectedResponse: LiveData<Result<Response<Unit>>> = _connectedResponse


    val isBasicInfoComplete = MutableLiveData(false)

    fun setBasicInfoComplete(isComplete: Boolean) {
        isBasicInfoComplete.value = isComplete
    }

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

    fun connectVehicleToPhoto(vehicleId: Int, filePath: String) {
        viewModelScope.launch {
            try {
                when (val result = vehicleRepository.connectVehicleToPhoto(vehicleId, filePath)) {
                    is Result.Success -> {
                        _connectedResponse.postValue(Result.Success(result.data))
                    }
                    is Result.Error -> {
                        _connectedResponse.postValue(Result.Error(result.error))
                    }
                }
            } catch (e: Exception) {
                _connectedResponse.postValue(Result.Error(e))
            }
        }
    }

    fun uploadPhoto(file: File) {
        viewModelScope.launch {
            try {
                when (val result = vehicleRepository.uploadPhoto(file)) {
                    is Result.Success -> {
                        val filePath = result.data.body()?.filePath
                        if (filePath != null) {
                            val response = Response.success(filePath)
                            _uploadResponse.postValue(Result.Success(response))
                        } else {
                            _uploadResponse.postValue(Result.Error(Exception("File path is null")))
                        }

                    }
                    is Result.Error -> {
                        _uploadResponse.postValue(Result.Error(result.error))
                    }
                }
            } catch (e: Exception) {
                _uploadResponse.postValue(Result.Error(e))
            }
        }
    }

    fun setVehicleId(id: Int) {
        _vehicleId.value = id
    }

    fun getVehicleId(): Int? {
        return _vehicleId.value
    }

    fun getVehicleIdByReg(reg: String) {
        viewModelScope.launch {
            val vehicleId = when (val result = vehicleRepository.getVehicleIdByReg(reg)) {
                is Result.Success -> {
                    println(result.data.body())
                    val responseBody = result.data.body()
                    responseBody?.firstOrNull()?.id
                }
                is Result.Error -> {
                    Log.e("VehicleCatalogViewModel", "Error fetching vehicle: ${result.error}")
                    null
                }
            }
            vehicleId?.let { setVehicleId(it) }
        }
    }
}