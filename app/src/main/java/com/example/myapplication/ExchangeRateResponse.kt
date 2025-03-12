// ExchangeRateResponse.kt
package com.example.myapplication

import com.google.gson.annotations.SerializedName

data class ExchangeRateResponse(
    @SerializedName("result") val result: String?,
    @SerializedName("provider") val provider: String?,
    @SerializedName("documentation") val documentation: String?,
    @SerializedName("terms_of_use") val termsOfUse: String?,
    @SerializedName("time_last_update_unix") val timeLastUpdateUnix: Long?,
    @SerializedName("time_last_update_utc") val timeLastUpdateUtc: String?,
    @SerializedName("time_next_update_unix") val timeNextUpdateUnix: Long?,
    @SerializedName("time_next_update_utc") val timeNextUpdateUtc: String?,
    @SerializedName("time_eol_unix") val timeEolUnix: Long?,
    @SerializedName("base_code") val baseCode: String?,
    @SerializedName("rates") val rates: Map<String, Double>?
)