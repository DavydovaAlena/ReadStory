package ru.adavydova.booksmart.presentation.screens.inactive_search_book_screen.filters

data class FilterBookState(
    val orderType: OrderBooks,
    val filter: FilterBooks,
    val languageRestrict: LanguageRestrictFilterBooks
)