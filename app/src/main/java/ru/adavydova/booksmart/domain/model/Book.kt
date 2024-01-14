package ru.adavydova.booksmart.domain.model

data class Book(
    val id: String,
    val volumeInfo: VolumeInfo,
    val saleInfo: SaleInfo,
    val accessInfo: AccessInfo
)