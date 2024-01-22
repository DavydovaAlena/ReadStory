package ru.adavydova.booksmart.domain.usecase


data class BooksUseCase(
    val searchBookUseCase: SearchBookUseCase,
    val filterBooksUseCase: FilterBooksUseCase,
    val getBookByIdUseCase: GetBookByIdUseCase

)

