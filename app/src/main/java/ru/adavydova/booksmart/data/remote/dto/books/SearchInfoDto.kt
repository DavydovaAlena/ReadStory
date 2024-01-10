package ru.adavydova.booksmart.data.remote.dto.books


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchInfoDto(
    @Json(name = "textSnippet")
    val textSnippet: String?
)