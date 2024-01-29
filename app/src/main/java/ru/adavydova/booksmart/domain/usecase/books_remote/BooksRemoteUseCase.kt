package ru.adavydova.booksmart.domain.usecase.books_remote


data class BooksRemoteUseCase(
    val searchBookUseCase: SearchBookUseCase,
    val filterBooksUseCase: FilterBooksUseCase,
    val getBookByIdUseCase: GetBookByIdUseCase

)

