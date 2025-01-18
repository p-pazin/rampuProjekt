package com.example.carchive.viewmodels

import androidx.lifecycle.ViewModel
import com.example.carchive.data.repositories.AdRepository
import androidx.lifecycle.viewModelScope
import com.example.carchive.data.network.Result
import com.example.carchive.entities.Ad
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdViewModel : ViewModel() {
    private val adRepository = AdRepository()

    private val _ads = MutableStateFlow<List<Ad>>(emptyList())
    val ads: StateFlow<List<Ad>> = _ads

    fun fetchAds() {
        viewModelScope.launch {
            val adsFromRepository = when (val result = adRepository.getAds()) {
                is Result.Success -> result.data
                is Result.Error -> listOf()
            }
            _ads.update { adsFromRepository }
        }
    }
}
