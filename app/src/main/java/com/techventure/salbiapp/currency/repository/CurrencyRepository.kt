package com.techventure.salbiapp.currency.repository

import com.techventure.salbiapp.currency.utils.Resource
import com.techventure.salbiapp.currencyApp.data.model.CurrencyResponse

interface CurrencyRepository {
    suspend fun getRates(base: String): Resource<CurrencyResponse>
}