package com.example.mipt_5_praktinis.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.mipt_5_praktinis.data.CurrencyEntity
import com.example.mipt_5_praktinis.data.CurrencyLoader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class CurrencyViewModel : ViewModel() {
    private val _currencyList = MutableStateFlow<List<CurrencyEntity>>(emptyList())
    val currencyList: StateFlow<List<CurrencyEntity>> = _currencyList.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val list = CurrencyLoader.fetchCurrencyList()
            if (list.isNotEmpty()) {
                _currencyList.value = list
            }
        }
    }
}