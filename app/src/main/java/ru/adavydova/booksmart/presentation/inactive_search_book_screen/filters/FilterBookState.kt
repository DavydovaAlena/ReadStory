package ru.adavydova.booksmart.presentation.inactive_search_book_screen.filters

data class FilterBookState(
    val orderType: OrderBooks,
    val filter: FilterBooks,
    val languageRestrict: LanguageRestrictFilterBooks
)