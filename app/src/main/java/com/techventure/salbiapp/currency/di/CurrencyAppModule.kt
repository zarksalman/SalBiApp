package com.techventure.salbiapp.currency.di

import com.techventure.salbiapp.currency.interfaces.dispatchers.DispatcherProvider
import com.techventure.salbiapp.currency.interfaces.retrofit.CurrencyApi
import com.techventure.salbiapp.currency.repository.CurrencyRepository
import com.techventure.salbiapp.currency.repository.CurrencyRepositoryImp
import com.techventure.salbiapp.currency.utils.NetworkUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "https://api.exchangeratesapi.io/"

@Module
@InstallIn(SingletonComponent::class)
object CurrencyAppModule {

    @Singleton
    @Provides
    fun provideCurrencyApi(): CurrencyApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(NetworkUtils.getRetrofitClient())
        .build()
        .create(CurrencyApi::class.java)

    @Singleton
    @Provides
    fun provideRepository(api: CurrencyApi): CurrencyRepository = CurrencyRepositoryImp(api)

    @Singleton
    @Provides
    fun provideDispatcher(): DispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined

    }
}