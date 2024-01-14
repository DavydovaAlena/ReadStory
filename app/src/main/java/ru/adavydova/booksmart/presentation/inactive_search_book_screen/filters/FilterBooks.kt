package ru.adavydova.booksmart.presentation.inactive_search_book_screen.filters

sealed class FilterBooks(val filter: String){
    object Ebooks: FilterBooks(filter = "ebooks")
    object FreeEbooks: FilterBooks(filter = "free-ebooks")
    object Full: FilterBooks(filter = "full")
    object PaidEbooks: FilterBooks(filter = "paid-ebooks")
    object Partial: FilterBooks(filter = "partial")

}