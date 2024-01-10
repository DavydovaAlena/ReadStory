package ru.adavydova.booksmart.data.remote.dto.books


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DimensionsDto(
    @Json(name = "height")
    val height: String?,
    @Json(name = "thickness")
    val thickness: String?,
    @Json(name = "width")
    val width: String?
)