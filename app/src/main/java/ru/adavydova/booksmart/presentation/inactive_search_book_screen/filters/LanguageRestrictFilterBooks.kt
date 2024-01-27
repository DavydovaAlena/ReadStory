package ru.adavydova.booksmart.presentation.inactive_search_book_screen.filters


interface FilterTypeBook

sealed class LanguageRestrictFilterBooks(val language: String): FilterTypeBook {
    object RuBooks: LanguageRestrictFilterBooks(language = "ru")
    object EnBooks: LanguageRestrictFilterBooks(language = "en")
    object FrBooks: LanguageRestrictFilterBooks(language = "fr")
    object DeBooks: LanguageRestrictFilterBooks(language = "de")
    object AllLanguage: LanguageRestrictFilterBooks(language = "all")

}

fun String.selectLanguageRestrict(): LanguageRestrictFilterBooks{
   return when(this){
        LanguageRestrictFilterBooks.RuBooks.language -> LanguageRestrictFilterBooks.RuBooks
        LanguageRestrictFilterBooks.FrBooks.language -> LanguageRestrictFilterBooks.FrBooks
        LanguageRestrictFilterBooks.EnBooks.language -> LanguageRestrictFilterBooks.EnBooks
        LanguageRestrictFilterBooks.AllLanguage.language -> LanguageRestrictFilterBooks.AllLanguage
        LanguageRestrictFilterBooks.DeBooks.language -> LanguageRestrictFilterBooks.DeBooks
        else -> throw IllegalArgumentException("incorrect value")
    }
}