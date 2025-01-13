package com.example.carchive.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carchive.data.dto.ContactStatusStatsDto
import com.example.carchive.data.dto.YearlyInfoDto
import com.example.carchive.data.network.Result
import com.example.carchive.data.repositories.ContactRepository
import com.example.carchive.data.repositories.StatsRepository
import com.example.carchive.entities.Contact
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StatsViewModel : ViewModel() {
    private val statsRepository = StatsRepository()
    private val _contactStatusStatsData = MutableStateFlow<ContactStatusStatsDto?>(null)
    val contactStatusStatsData = _contactStatusStatsData.asStateFlow()
    private val _contactCreationStatsData = MutableStateFlow<YearlyInfoDto?>(null)
    val contactCreationStatsData = _contactCreationStatsData.asStateFlow()
    private val _invoiceCreationStatsData = MutableStateFlow<YearlyInfoDto?>(null)
    val invoiceCreationStatsData = _invoiceCreationStatsData.asStateFlow()

    fun fetchContactStatusStats() {
        viewModelScope.launch {
            val stats = when (val result = statsRepository.getContactStatusStats()) {
                is Result.Success -> result.data
                is Result.Error -> null
            }
            _contactStatusStatsData.update { stats }
        }
    }

    fun fetchContactCreationStats() {
        viewModelScope.launch {
            val stats = when (val result = statsRepository.getContactCreationStats()) {
                is Result.Success -> result.data
                is Result.Error -> null
            }
            _contactCreationStatsData.update { stats }
        }
    }

    fun fetchInvoiceCreationStats() {
        viewModelScope.launch {
            val stats = when (val result = statsRepository.getInvoiceCreationStats()) {
                is Result.Success -> result.data
                is Result.Error -> null
            }
            _invoiceCreationStatsData.update { stats }
        }
    }
}