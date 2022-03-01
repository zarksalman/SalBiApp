package com.techventure.androidbase.network.model


enum class Error(private val code: Int, private val err: String) {

    UNKNOWN(1000, "Unknown Error, please try again later"),
    PARSE_ERROR(1001, "Parsing Error, please try again later"),
    NETWORK_ERROR(1002, "Network Error, please try again later"),
    SSL_ERROR(1004, "SSL ERROR, please try again later"),

    TIMEOUT_ERROR(1006, "Network connection timed out, please try again later");

    fun getValue(): String {
        return err
    }

    fun getKey(): Int {
        return code
    }

}