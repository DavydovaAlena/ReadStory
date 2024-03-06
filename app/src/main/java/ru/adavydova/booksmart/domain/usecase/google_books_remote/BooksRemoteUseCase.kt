package ru.adavydova.booksmart.domain.usecase.google_books_remote


data class BooksRemoteUseCase(
    val searchBookUseCase: SearchBookUseCase,
    val filterBooksUseCase: FilterBooksUseCase,
    val getBookByIdUseCase: GetBookByIdUseCase

)

