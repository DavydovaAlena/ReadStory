package ru.adavydova.booksmart.presentation.inactive_search_book_screen.filters

sealed class LanguageRestrictFilterBooks(val language: String) {
    object RuBooks: LanguageRestrictFilterBooks(language = "ru")
    object EnBooks: LanguageRestrictFilterBooks(language = "en")
    object FrBooks: LanguageRestrictFilterBooks(language = "fr")
    object DeBooks: LanguageRestrictFilterBooks(language = "de")
}