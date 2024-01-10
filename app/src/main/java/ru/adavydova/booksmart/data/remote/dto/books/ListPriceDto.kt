package ru.adavydova.booksmart.data.remote.dto.books


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ListPriceDto(
    @Json(name = "amount")
    val amount: Double,
    @Json(name = "currencyCode")
    val currencyCode: String
)