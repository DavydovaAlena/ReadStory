package ru.adavydova.booksmart.domain.model

import androidx.room.Entity

@Entity
data class Dimensions (
    val height: String?,
    val width: String?,
    val thickness: String?
)