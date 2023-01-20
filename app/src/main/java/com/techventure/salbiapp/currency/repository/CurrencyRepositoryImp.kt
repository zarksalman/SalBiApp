package com.techventure.salbiapp.currency.repository

import com.techventure.salbiapp.currency.interfaces.retrofit.CurrencyApi
import com.techventure.salbiapp.currency.utils.Resource
import com.techventure.salbiapp.currencyApp.data.model.CurrencyResponse
import javax.inject.Inject

class CurrencyRepositoryImp @Inject constructor(
    private val api: CurrencyApi
) : CurrencyRepository {
    override suspend fun getRates(base: String): Resource<CurrencyResponse> {
        return try {
            val response = api.getRates(base)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(data = result)
            } else {
                Resource.Error(message = response.message())
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "Error occurred")
        }
    }
}