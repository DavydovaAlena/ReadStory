package ru.adavydova.booksmart.data.remote.dto.books


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EpubDto(
    @Json(name = "acsTokenLink")
    val acsTokenLink: String,
    @Json(name = "downloadLink")
    val downloadLink: String,
    @Json(name = "isAvailable")
    val isAvailable: Boolean
)