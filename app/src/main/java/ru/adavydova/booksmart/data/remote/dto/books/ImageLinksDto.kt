package ru.adavydova.booksmart.data.remote.dto.books


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageLinksDto(
    @Json(name = "extraLarge")
    val extraLarge: String?,
    @Json(name = "large")
    val large: String?,
    @Json(name = "medium")
    val medium: String?,
    @Json(name = "small")
    val small: String?,
    @Json(name = "smallThumbnail")
    val smallThumbnail: String?,
    @Json(name = "thumbnail")
    val thumbnail: String?
)