package ru.adavydova.booksmart.domain.model

import java.util.Locale.Category

data class VolumeInfo(
    val title: String,
    val subtitle: String,
    val authors: List<String>,
    val publisher: String,
    val publishedDate: String?,
    val description: String,
    val pageCount: Int,
    val dimensions: Dimensions,
    val mainCategory: String,
    val categories: List<String>,
    val imageLinks: String,
    val averageRating: Double?,
    val ratingsCount: Int,
    val language: String,
    val previewLink: String,
    val infoLink: String,
    val canonicalVolumeLink: String

)
