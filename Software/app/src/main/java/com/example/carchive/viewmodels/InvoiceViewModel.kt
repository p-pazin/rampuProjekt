package com.example.carchive.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carchive.data.dto.InvoiceDto
import com.example.carchive.data.network.Result
import com.example.carchive.data.repositories.InvoiceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InvoiceViewModel: ViewModel() {
    private val invoiceRepository: InvoiceRepository = InvoiceRepository()

    private val _invoices = MutableStateFlow<List<InvoiceDto>>(listOf())
    val invoices = _invoices.asStateFlow()

    fun fetchInvoices() {
        viewModelScope.launch {
            val invoicesFromRepository = when (val result = invoiceRepository.getInvoices()) {
                is Result.Success -> result.data
                is Result.Error -> {
                    Log.e("VehicleCatalogViewModel", "Greška prilikom dohvaćanja računa: ${result.error}")
                    listOf()
                }
            }
            Log.d("InvoiceVM", "Dohvaćeni računi: $invoicesFromRepository")
            _invoices.update { invoicesFromRepository }
        }
    }
}