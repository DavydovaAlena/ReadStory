package ru.adavydova.booksmart.data.remote.dto.books


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ItemDto(
    @Json(name = "accessInfo")
    val accessInfo: AccessInfoDto,
    @Json(name = "etag")
    val etag: String,
    @Json(name = "id")
    val id: String,
    @Json(name = "kind")
    val kind: String,
    @Json(name = "saleInfo")
    val saleInfo: SaleInfoDto,
    @Json(name = "searchInfo")
    val searchInfo: SearchInfoDto?,
    @Json(name = "selfLink")
    val selfLink: String,
    @Json(name = "userInfo")
    val userInfo: UserInfoDto?,
    @Json(name = "volumeInfo")
    val volumeInfo: VolumeInfoDto
)