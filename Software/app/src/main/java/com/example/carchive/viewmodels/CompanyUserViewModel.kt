package com.example.carchive.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carchive.data.dto.CompanyDto
import com.example.carchive.data.dto.NewPasswordDto
import com.example.carchive.data.dto.NewUserDto
import com.example.carchive.data.dto.UserDto
import com.example.carchive.data.network.Result
import com.example.carchive.data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Response

class CompanyUserViewModel : ViewModel() {

    private val userRepository = UserRepository()

    private val _user = MutableStateFlow<UserDto>(UserDto.EMPTY)
    val user: StateFlow<UserDto> = _user

    private val _company = MutableStateFlow<CompanyDto>(CompanyDto.EMPTY)
    val company: StateFlow<CompanyDto> = _company

    private val _companyUsers = MutableStateFlow<List<UserDto>>(emptyList())
    val companyUsers: StateFlow<List<UserDto>> = _companyUsers

    private val _deleteUserResult = MutableLiveData<Result<Response<Unit>>>()
    val deleteUserResult: LiveData<Result<Response<Unit>>> = _deleteUserResult

    private val _newUserResult = MutableLiveData<Result<Response<Unit>>>()
    val newUserResult: LiveData<Result<Response<Unit>>> = _newUserResult

    private val _newUserPasswordResult = MutableLiveData<Result<Response<Unit>>>()
    val newUserPasswordResult: LiveData<Result<Response<Unit>>> = _newUserPasswordResult

    fun setUser(user: UserDto) {
        _user.value = user
    }

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

    fun fetchCompanyUsers() {
        viewModelScope.launch {
            val usersFromRepository = when (val result = userRepository.getCompanyUsers()) {
                is Result.Success -> result.data
                is Result.Error -> emptyList()
            }
            _companyUsers.update { usersFromRepository }
        }
    }

    fun deleteUserFromCompany(id: Int) {
        viewModelScope.launch {
            try {
                when (val result = userRepository.deleteUser(id)) {
                    is Result.Success -> {
                        fetchCompanyUsers()
                    }
                    is Result.Error -> {
                        _deleteUserResult.postValue(Result.Error(result.error))
                    }
                }
            } catch (e: Exception) {
                _deleteUserResult.postValue(Result.Error(e))
            }
        }
    }
    fun addNewUser(newUserDto: NewUserDto) {
        viewModelScope.launch {
            try {
                val result = userRepository.registerNewUser(newUserDto)
                if (result is Result.Success) {
                    _newUserResult.postValue(Result.Success(result.data))
                }
                if (result is Result.Error) {
                    _newUserResult.postValue(Result.Error(result.error))
                }
            } catch (e: Exception) {
                _newUserResult.postValue(Result.Error(e))
            }
        }
    }
    fun updateUser(user: UserDto) {
        viewModelScope.launch {
            try {
                val result = userRepository.updateUser(user)
                if (result is Result.Success) {
                    _newUserResult.postValue(Result.Success(result.data))
                }
                if (result is Result.Error) {
                    _newUserResult.postValue(Result.Error(result.error))
                }
            } catch (e: Exception) {
                _newUserResult.postValue(Result.Error(e))
            }
        }
    }

    fun changeUserPassword(newPasswordDto: NewPasswordDto) {
        viewModelScope.launch {
            try {
                val result = userRepository.newpasswordUser(newPasswordDto)
                if (result is Result.Success) {
                    _newUserPasswordResult.postValue(Result.Success(result.data))
                } else if (result is Result.Error) {
                    _newUserPasswordResult.postValue(Result.Error(result.error))
                }
            } catch (e: Exception) {
                _newUserPasswordResult.postValue(Result.Error(e))
            }
        }
    }



}