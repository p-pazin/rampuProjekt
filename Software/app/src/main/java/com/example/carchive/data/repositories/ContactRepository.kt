package com.example.carchive.data.repositories

import com.example.carchive.data.dto.ContactDto
import com.example.carchive.data.dto.toEntity
import com.example.carchive.data.network.Network
import com.example.carchive.data.network.Result
import com.example.carchive.entities.Contact
import com.example.carchive.util.safeResponse
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.http.HTTP

class ContactRepository {
    private val networkClient = Network().getInstance()

    suspend fun getContacts(): Result <List<Contact>> {
        return safeResponse {
            networkClient.getContacts().map {
                it.toEntity()
            }
        }
    }

    suspend fun postContact(contactDto: ContactDto): Result<Response<Unit>> {
        return safeResponse {
            networkClient.postContact(contactDto)
        }
    }

    suspend fun putContact(contactDto: ContactDto): Result<Response<Unit>> {
        return safeResponse {
            networkClient.putContact(contactDto.id, contactDto)
        }
    }

    suspend fun deleteContact(contactId: Int): Result<Response<Unit>> {
        return safeResponse {
            networkClient.deleteContact(contactId)
        }
    }
}