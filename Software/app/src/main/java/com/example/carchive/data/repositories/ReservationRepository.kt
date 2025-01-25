package com.example.carchive.data.repositories

import com.example.carchive.data.dto.ReservationDto
import com.example.carchive.data.network.Network
import com.example.carchive.data.network.Result
import com.example.carchive.util.safeResponse
import retrofit2.Response

class ReservationRepository {
    private val networkClient = Network().getInstance()

    suspend fun getReservations(): Result<List<ReservationDto>> {
        return safeResponse {
            networkClient.getReservations()
        }
    }

    suspend fun getReservation(id: Int): Result<ReservationDto> {
        return safeResponse {
            networkClient.getReservation(id)
        }
    }

    suspend fun postReservation(reservation: ReservationDto): Result<Response<Unit>> {
        return safeResponse {
            networkClient.postReservation(reservation.contactId, reservation.vehicleId, reservation)
        }
    }

    suspend fun putReservation(reservation: ReservationDto): Result<Response<Unit>> {
        return safeResponse {
            networkClient.putReservation(reservation.id, reservation.vehicleId, reservation)
        }
    }

    suspend fun deleteReservation(id: Int): Result<Response<Unit>> {
        return safeResponse {
            networkClient.deleteReservation(id)
        }
    }
}