package com.example.carchive.fragments

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carchive.R
import com.example.carchive.data.dto.ContactDto
import com.example.carchive.data.network.Result
import com.example.carchive.data.repositories.ContactRepository
import com.example.carchive.entities.Contact
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date

class ContactsViewModel : ViewModel() {
    private val contactRepository = ContactRepository()
    private val _contacts = MutableStateFlow<List<Contact>>(listOf())
    val contacts = _contacts.asStateFlow()

    private val _postResult = MutableLiveData<Result<Response<Unit>>>()
    val postResult: LiveData<Result<Response<Unit>>> = _postResult
    private val _putResult = MutableLiveData<Result<Response<Unit>>>()
    val putResult: LiveData<Result<Response<Unit>>> = _putResult
    private val _deleteResult = MutableLiveData<Result<Response<Unit>>>()
    val deleteResult: LiveData<Result<Response<Unit>>> = _deleteResult

    fun fetchContacts() {
        viewModelScope.launch {
            val contactsFromRepository = when (val result = contactRepository.getContacts()) {
                is Result.Success -> result.data
                is Result.Error -> listOf()
            }
            _contacts.update { contactsFromRepository }
        }
    }

    fun postContact(
        firstName: String,
        lastName: String,
        pin: String,
        address: String,
        telephoneNumber: String,
        mobileNumber: String,
        email: String,
        description: String,
        country: String,
        city: String,
        state: Int
    ) {

        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val contactDto = ContactDto(
            firstName = firstName,
            lastName = lastName,
            pin = pin,
            address = address,
            telephoneNumber = telephoneNumber,
            mobileNumber = mobileNumber,
            email = email,
            description = description,
            country = country,
            city = city,
            state = state,
            id = 0,
            dateOfCreation = simpleDateFormat.format(Date())
        )

        viewModelScope.launch {
            try {
                when (val result = contactRepository.postContact(contactDto)) {
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

    fun putContact(
        firstName: String,
        lastName: String,
        pin: String,
        address: String,
        telephoneNumber: String,
        mobileNumber: String,
        email: String,
        description: String,
        country: String,
        city: String,
        state: String,
        dateOfCreation: String,
        id: Int
    ) {

        val contactDto = ContactDto(
            firstName = firstName,
            lastName = lastName,
            pin = pin,
            address = address,
            telephoneNumber = telephoneNumber,
            mobileNumber = mobileNumber,
            email = email,
            description = description,
            country = country,
            city = city,
            state = if (state == "Aktivni kontakt") 1 else 0,
            id = id,
            dateOfCreation = dateOfCreation
        )

        viewModelScope.launch {
            try {
                when (val result = contactRepository.putContact(contactDto)) {
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

    fun deleteContact(contactId: Int) {

        viewModelScope.launch {
            try {
                when (val result = contactRepository.deleteContact(contactId)) {
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

    fun validateInputs(
        firstName: String,
        lastName: String,
        pin: String,
        address: String,
        telephoneNumber: String,
        mobileNumber: String,
        email: String,
        description: String
    ): Boolean {
        return firstName.isNotEmpty() &&
                lastName.isNotEmpty() &&
                pin.isNotEmpty() &&
                address.isNotEmpty() &&
                telephoneNumber.isNotEmpty() &&
                mobileNumber.isNotEmpty() &&
                email.isNotEmpty() &&
                description.isNotEmpty()
    }
}