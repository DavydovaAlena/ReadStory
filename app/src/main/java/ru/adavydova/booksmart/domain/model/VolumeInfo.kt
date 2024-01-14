package ru.adavydova.booksmart.domain.model


data class VolumeInfo(
    val title: String,
    val authors: List<String>,
    val publisher: String?,
    val publishedDate: String?,
    val description: String?,
    val pageCount: Int,
    val categories: List<String>?,
    val imageLinks: String?,
    val language: String,
)
