package com.techventure.salbiapp.currencyApp.data.model

data class CurrencyResponse(
    val base: String,
    val date: String,
    val rate: HashMap<String, Double>
)