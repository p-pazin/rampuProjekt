package com.example.carchive.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carchive.data.dto.CompanyDto
import com.example.carchive.data.dto.UserDto
import com.example.carchive.data.network.Result
import com.example.carchive.data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CompanyUserViewModel : ViewModel() {

    private val userRepository = UserRepository()

    private val _user = MutableStateFlow<UserDto>(UserDto.EMPTY)
    val user: StateFlow<UserDto> = _user

    private val _company = MutableStateFlow<CompanyDto>(CompanyDto.EMPTY)
    val company: StateFlow<CompanyDto> = _company

    fun fetchUser() {
        viewModelScope.launch {
            val userFromRepository = when (val result = userRepository.getUser()) {
                is Result.Success -> result.data
                is Result.Error -> UserDto.EMPTY
            }
            _user.update { userFromRepository }
        }
    }

    fun fetchCompany() {
        viewModelScope.launch {
            val companyFromRepository = when (val result = userRepository.getCompany()) {
                is Result.Success -> result.data
                is Result.Error -> CompanyDto.EMPTY
            }
            _company.update { companyFromRepository }
        }
    }
}