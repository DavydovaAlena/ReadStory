package ru.adavydova.booksmart.presentation.inactive_search_book_screen.filters

sealed class FilterBooks(val filter: String){
    object Ebooks: FilterBooks(filter = "ebooks")
    object FreeEbooks: FilterBooks(filter = "free-ebooks")
    object Full: FilterBooks(filter = "full")
    object PaidEbooks: FilterBooks(filter = "paid-ebooks")
    object Partial: FilterBooks(filter = "partial")

    operator fun invoke(): List<FilterBooks>{
        return listOf(
            Ebooks,
            FreeEbooks,
            Full,
            PaidEbooks,
            Partial
        )
    }
}

fun String.selectFilterType(): FilterBooks {
    return when(this){
       FilterBooks.Full.filter -> FilterBooks.Full
       FilterBooks.Partial.filter -> FilterBooks.Partial
       FilterBooks.Ebooks.filter -> FilterBooks.Ebooks
       FilterBooks.PaidEbooks.filter -> FilterBooks.PaidEbooks
       FilterBooks.FreeEbooks.filter -> FilterBooks.FreeEbooks
       else -> throw IllegalArgumentException("incorrect value")
    }
}