package ru.adavydova.booksmart.data.remote.dto.books


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserInfoDto(
    @Json(name = "isPreordered")
    val isPreordered: Boolean?,
    @Json(name = "isPurchased")
    val isPurchased: Boolean?,
    @Json(name = "readingPosition")
    val readingPosition: String?,
    @Json(name = "review")
    val review: String?,
    @Json(name = "updated")
    val updated: String?
)