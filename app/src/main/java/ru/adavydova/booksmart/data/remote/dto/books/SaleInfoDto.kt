package ru.adavydova.booksmart.data.remote.dto.books


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SaleInfoDto(
    @Json(name = "buyLink")
    val buyLink: String?,
    @Json(name = "country")
    val country: String,
    @Json(name = "isEbook")
    val isEbook: Boolean,
    @Json(name = "listPrice")
    val listPrice: ListPriceDto?,
    @Json(name = "onSaleDate")
    val onSaleDate: String?,
    @Json(name = "retailPrice")
    val retailPrice: RetailPriceDto?,
    @Json(name = "saleability")
    val saleability: String
)