package ru.adavydova.booksmart.data.remote.dto.books


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AccessInfoDto(
    @Json(name = "accessViewStatus")
    val accessViewStatus: String,
    @Json(name = "country")
    val country: String,
    @Json(name = "downloadAccess")
    val downloadAccess: DownloadAccessDto,
    @Json(name = "embeddable")
    val embeddable: Boolean,
    @Json(name = "epub")
    val epub: EpubDto,
    @Json(name = "pdf")
    val pdf: PdfDto,
    @Json(name = "publicDomain")
    val publicDomain: Boolean,
    @Json(name = "textToSpeechPermission")
    val textToSpeechPermission: String,
    @Json(name = "viewability")
    val viewability: String,
    @Json(name = "webReaderLink")
    val webReaderLink: String
)