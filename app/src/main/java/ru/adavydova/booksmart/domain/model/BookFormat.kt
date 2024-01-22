package ru.adavydova.booksmart.domain.model



data class BookFormat(
    val isAvailable: Boolean?,
    val acsTokenLink:String?,
    val downloadLink: String?
)