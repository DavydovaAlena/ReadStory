package ru.adavydova.booksmart.domain.model

import androidx.room.Entity


@Entity
data class Book(
    val id: String,
    val volumeInfo: VolumeInfo,
    val saleInfo: SaleInfo,
    val accessInfo: AccessInfo
)