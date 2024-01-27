package ru.adavydova.booksmart.data.remote.dto.books


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DownloadAccessDto(
    @Json(name = "deviceAllowed")
    val deviceAllowed: Boolean?,
    @Json(name = "downloadsAcquired")
    val downloadsAcquired: Int?,
    @Json(name = "justAcquired")
    val justAcquired: Boolean?,
    @Json(name = "kind")
    val kind: String?,
    @Json(name = "maxDownloadDevices")
    val maxDownloadDevices: Int?,
    @Json(name = "message")
    val message: String?,
    @Json(name = "nonce")
    val nonce: String?,
    @Json(name = "reasonCode")
    val reasonCode: String?,
    @Json(name = "restricted")
    val restricted: Boolean?,
    @Json(name = "signature")
    val signature: String?,
    @Json(name = "source")
    val source: String?,
    @Json(name = "volumeId")
    val volumeId: String?
)