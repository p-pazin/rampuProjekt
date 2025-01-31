package com.example.carchive.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carchive.data.dto.ContractDetailedRentDto
import com.example.carchive.data.dto.ContractDetailedSaleDto
import com.example.carchive.data.dto.ContractDto
import com.example.carchive.data.dto.InvoiceDto
import com.example.carchive.data.dto.InvoiceDtoPost
import com.example.carchive.data.network.Result
import com.example.carchive.data.repositories.InvoiceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date

class InvoiceViewModel: ViewModel() {
    private val invoiceRepository: InvoiceRepository = InvoiceRepository()

    private val _invoices = MutableStateFlow<List<InvoiceDto>>(listOf())
    val invoices = _invoices.asStateFlow()

    private val _invoiceSale = MutableLiveData<Result<Response<Unit>>>()
    val invoiceSale: LiveData<Result<Response<Unit>>> = _invoiceSale
    private val _invoiceRentStart = MutableLiveData<Result<Response<Unit>>>()
    val invoiceRentStart: LiveData<Result<Response<Unit>>> = _invoiceRentStart
    private val _invoiceRentEnd = MutableLiveData<Result<Response<Unit>>>()
    val invoiceRentEnd: LiveData<Result<Response<Unit>>> = _invoiceRentEnd

    private val _validationResult = MutableLiveData<Boolean>()
    val validationResult: LiveData<Boolean> get() = _validationResult

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

    suspend fun postInvoice(invoiceType: Int, contractId: Int, invoice: InvoiceDtoPost) {
        if(invoiceType == 1) {
            try {
                when (val result = invoiceRepository.postInvoiceSale(contractId, invoice)) {
                    is Result.Success -> {
                        _invoiceSale.postValue(Result.Success(result.data))
                    }
                    is Result.Error -> {
                        _invoiceSale.postValue(Result.Error(result.error))
                    }
                }
            } catch (e: Exception) {
                _invoiceSale.postValue(Result.Error(e))
            }
        }
    }

    suspend fun postInvoiceRentStart(invoiceType: Int, contractId: Int, invoice: InvoiceDtoPost) {
        if(invoiceType == 2) {
            try {
                when (val result = invoiceRepository.postInvoiceRentStart(contractId, invoice)) {
                    is Result.Success -> {
                        _invoiceRentStart.postValue(Result.Success(result.data))
                    }
                    is Result.Error -> {
                        _invoiceRentStart.postValue(Result.Error(result.error))
                    }
                }
            } catch (e: Exception) {
                _invoiceRentStart.postValue(Result.Error(e))
            }
        }
    }

    suspend fun postInvoiceRentEnd(invoiceType: Int, contractId: Int, invoice: InvoiceDtoPost, penalties : List<Int>) {
        if(invoiceType == 2) {
            try {
                when (val result = invoiceRepository.postInvoiceRentEnd(contractId, invoice,penalties)) {
                    is Result.Success -> {
                        _invoiceRentEnd.postValue(Result.Success(result.data))
                    }
                    is Result.Error -> {
                        _invoiceRentEnd.postValue(Result.Error(result.error))
                    }
                }
            } catch (e: Exception) {
                _invoiceRentEnd.postValue(Result.Error(e))
            }
        }
    }

    fun validateInvoiceInputs(
        contractType: Int,
        contractIdRent: Int?,
        contractIdSell: Int?,
        paymentMethodRent: String?,
        vatRent: String?,
        mileage: String?,
        paymentMethodSell: String?,
        vatSell: String?,
        totalSell: String?,
        isRentEnd: Boolean = false
    ) {
        val isValid = when (contractType) {
            1 -> { // Sell
                !paymentMethodSell.isNullOrEmpty() &&
                        !vatSell.isNullOrEmpty() &&
                        !totalSell.isNullOrEmpty() &&
                        contractIdSell != null
            }
            2 -> { // Rent
                !paymentMethodRent.isNullOrEmpty() &&
                        !vatRent.isNullOrEmpty() &&
                        contractIdRent != null &&
                        (!isRentEnd || !mileage.isNullOrEmpty())
            }
            else -> false
        }
        _validationResult.postValue(isValid)
    }

}