package ru.adavydova.booksmart.domain.usecase

import ru.adavydova.booksmart.domain.repository.BooksRepository

data class BooksUseCase(
    val searchBookUseCase: SearchBookUseCase,

)

