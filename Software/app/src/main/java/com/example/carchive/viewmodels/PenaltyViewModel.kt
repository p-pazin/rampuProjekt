package com.example.carchive.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carchive.data.dto.PenaltyDto
import com.example.carchive.data.network.Result
import com.example.carchive.data.repositories.PenaltyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PenaltyViewModel : ViewModel() {
    private val penaltiesRepository = PenaltyRepository()

    private val _penalties = MutableStateFlow<List<PenaltyDto>>(emptyList())
    val penalties: StateFlow<List<PenaltyDto>> = _penalties

    fun fetchPenalties() {
        viewModelScope.launch {
            val penaltiesFromRepository = when (val result = penaltiesRepository.getPenalties()) {
                is Result.Success -> result.data
                is Result.Error -> listOf()
            }
            _penalties.update { penaltiesFromRepository }
        }
    }
}