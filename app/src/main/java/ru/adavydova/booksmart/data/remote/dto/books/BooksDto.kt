package ru.adavydova.booksmart.data.remote.dto.books


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BooksDto(
    @Json(name = "items")
    val items: List<ItemDto>,
    @Json(name = "kind")
    val kind: String?,
    @Json(name = "totalItems")
    val totalItems: Int
)