package ru.adavydova.booksmart.domain.model

data class AccessInfo(
    val epub: BookFormat,
    val pdf: BookFormat,
    val webReaderLink: String,
)
