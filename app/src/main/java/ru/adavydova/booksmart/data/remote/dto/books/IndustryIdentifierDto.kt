package ru.adavydova.booksmart.data.remote.dto.books


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IndustryIdentifierDto(
    @Json(name = "identifier")
    val identifier: String?,
    @Json(name = "type")
    val type: String?
)