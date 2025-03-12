// ExchangeRateApi.kt
package com.example.myapplication

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeRateApi {
    @GET("latest")
    fun getExchangeRates(
        @Query("base") baseCurrency: String
    ): Call<ExchangeRateResponse>
}