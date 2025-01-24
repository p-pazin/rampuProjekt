package com.example.carchive.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carchive.data.dto.ContactDto
import com.example.carchive.data.dto.NewUserDto
import com.example.carchive.data.dto.ReservationDetails
import com.example.carchive.data.dto.ReservationDto
import com.example.carchive.data.dto.VehicleDto
import com.example.carchive.data.network.Result
import com.example.carchive.data.repositories.ContactRepository
import com.example.carchive.data.repositories.ReservationRepository
import com.example.carchive.data.repositories.VehicleRepository
import kotlinx.coroutines.async

import kotlinx.coroutines.launch
import retrofit2.Response

class ReservationViewModel : ViewModel() {

    private val reservationRepository = ReservationRepository()
    private val vehicleRepository = VehicleRepository()
    private val contactRepository = ContactRepository()

    private val _reservationsWithDetails = MutableLiveData<List<ReservationDetails>>(emptyList())
    val reservationsWithDetails: LiveData<List<ReservationDetails>> = _reservationsWithDetails

    private val _reservationDetails = MutableLiveData<ReservationDetails?>()
    val reservationDetails: LiveData<ReservationDetails?> get() = _reservationDetails

    private val _newReservation = MutableLiveData<Result<Response<Unit>>>()
    val newReservation: LiveData<Result<Response<Unit>>> = _newReservation

    fun fetchReservations() {
        viewModelScope.launch {
            when (val result = reservationRepository.getReservations()) {
                is Result.Success -> {
                    val reservations = result.data
                    val reservationsWithDetails = reservations.mapNotNull { reservation ->
                        try {
                            val vehicleDeferred = async { fetchVehicle(reservation.vehicleId) }
                            val contactDeferred = async { fetchContact(reservation.contactId) }

                            val vehicle = vehicleDeferred.await()
                            val contact = contactDeferred.await()

                            if (vehicle != null && contact != null) {
                                ReservationDetails(
                                    reservation = reservation,
                                    vehicle = vehicle,
                                    contact = contact
                                )
                            } else null
                        } catch (e: Exception) {
                            null
                        }
                    }
                    _reservationsWithDetails.postValue(reservationsWithDetails)
                }
                is Result.Error -> {
                    _reservationsWithDetails.postValue(emptyList())
                }
            }
        }
    }

    private suspend fun fetchVehicle(vehicleId: Int): VehicleDto? {
        return when (val result = vehicleRepository.getVehicleById(vehicleId)) {
            is Result.Success -> result.data
            is Result.Error -> {
                println("Failed to fetch vehicle with ID: $vehicleId")
                null
            }
        }
    }

    private suspend fun fetchContact(contactId: Int): ContactDto? {
        return when (val result = contactRepository.getContactById(contactId)) {
            is Result.Success -> result.data
            is Result.Error -> {
                println("Failed to fetch contact with ID: $contactId")
                null
            }
        }
    }

    fun fetchReservationDetails(reservationId: Int) {
        viewModelScope.launch {
            try {
                when (val result = reservationRepository.getReservation(reservationId)) {
                    is Result.Success -> {
                        val reservation = result.data
                        val vehicle = fetchVehicle(reservation.vehicleId)
                        val contact = fetchContact(reservation.contactId)

                        if (vehicle != null && contact != null) {
                            _reservationDetails.postValue(
                                ReservationDetails(
                                    reservation = reservation,
                                    vehicle = vehicle,
                                    contact = contact
                                )
                            )
                        } else {
                            _reservationDetails.postValue(null)
                        }
                    }
                    is Result.Error -> {
                        _reservationDetails.postValue(null)
                    }
                }
            } catch (e: Exception) {
                _reservationDetails.postValue(null)
            }
        }
    }

    fun addNewReservation(newReservation: ReservationDto) {
        viewModelScope.launch {
            try {
                val result = reservationRepository.postReservation(newReservation)
                if (result is Result.Success) {
                    _newReservation.postValue(Result.Success(result.data))
                }
                if (result is Result.Error) {
                    _newReservation.postValue(Result.Error(result.error))
                }
            } catch (e: Exception) {
                _newReservation.postValue(Result.Error(e))
            }
        }
    }
}

