package com.techventure.salbiapp.currency.utils

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor


object NetworkUtils {

    fun getRetrofitClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient().newBuilder()
            .addInterceptor(interceptor) //.addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
            .addNetworkInterceptor(Interceptor { chain ->
                val request: Request =
                    chain.request().newBuilder() // .addHeader(Constant.Header, authToken)
                        .build()
                chain.proceed(request)
            }).build()

    }
}