package com.techventure.salbiapp.currency.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techventure.salbiapp.currency.interfaces.dispatchers.DispatcherProvider
import com.techventure.salbiapp.currency.repository.CurrencyRepository
import com.techventure.salbiapp.currency.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.round

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val repositoryImp: CurrencyRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    sealed class CurrencyEvent {
        class Success(val resultText: String) : CurrencyEvent()
        class Error(val errorText: String) : CurrencyEvent()
        object Loading : CurrencyEvent()
        object Empty : CurrencyEvent()
    }

    private val _conversion = MutableStateFlow<CurrencyEvent>(CurrencyEvent.Empty)
    val conversion: StateFlow<CurrencyEvent> = _conversion

    fun convert(
        amount: String,
        fromCurrency: String,
        toCurrency: String
    ) {
        amount.toFloatOrNull()?.let {

            viewModelScope.launch(dispatchers.io) {
                _conversion.value = CurrencyEvent.Loading
                when (val rateResponse = repositoryImp.getRates(fromCurrency)) {
                    is Resource.Error -> _conversion.value =
                        CurrencyEvent.Error(rateResponse.message!!)
                    is Resource.Success -> {
                        val rates = rateResponse.data?.rate
                        val rate = rates?.get(toCurrency)

                        rate?.let {
                            val convertedCurrency = round(it * rate * 100) / 100
                            _conversion.value = CurrencyEvent.Success(
                                "$it $fromCurrency = $convertedCurrency $toCurrency"
                            )
                        } ?: run { _conversion.value = CurrencyEvent.Error("UnExpected Error") }
                    }
                }
            }

        } ?: run {
            _conversion.value = CurrencyEvent.Error("Not a valid number")
            return
        }
    }
}
