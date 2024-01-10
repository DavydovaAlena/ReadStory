package ru.adavydova.booksmart.domain.model

data class SaleInfo(
    val country: String?,
    val saleability: String?,
    val onSaleDate: String?,
    val price: Price?,
    val buyLink: String?
)
