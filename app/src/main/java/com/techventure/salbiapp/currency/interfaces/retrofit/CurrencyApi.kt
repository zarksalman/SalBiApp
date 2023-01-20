package com.techventure.salbiapp.currency.interfaces.retrofit

import com.techventure.salbiapp.currencyApp.data.model.CurrencyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {

    @GET("/latest")
    suspend fun getRates(
        @Query("Base") base: String
    ): Response<CurrencyResponse>
}