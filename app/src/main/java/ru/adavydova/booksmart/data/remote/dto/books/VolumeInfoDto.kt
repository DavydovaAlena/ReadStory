package ru.adavydova.booksmart.data.remote.dto.books


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VolumeInfoDto(
    @Json(name = "authors")
    val authors: List<String>,
    @Json(name = "averageRating")
    val averageRating: Double?,
    @Json(name = "canonicalVolumeLink")
    val canonicalVolumeLink: String?,
    @Json(name = "categories")
    val categories: List<String>,
    @Json(name = "contentVersion")
    val contentVersion: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "dimensions")
    val dimensions: DimensionsDto?,
    @Json(name = "imageLinks")
    val imageLinks: ImageLinksDto?,
    @Json(name = "industryIdentifiers")
    val industryIdentifiers: List<IndustryIdentifierDto>?,
    @Json(name = "infoLink")
    val infoLink: String?,
    @Json(name = "language")
    val language: String?,
    @Json(name = "mainCategory")
    val mainCategory: String?,
    @Json(name = "pageCount")
    val pageCount: Int?,
    @Json(name = "previewLink")
    val previewLink: String?,
    @Json(name = "printType")
    val printType: String?,
    @Json(name = "publishedDate")
    val publishedDate: String?,
    @Json(name = "publisher")
    val publisher: String?,
    @Json(name = "ratingsCount")
    val ratingsCount: Int?,
    @Json(name = "subtitle")
    val subtitle: String?,
    @Json(name = "title")
    val title: String?
)