package ru.adavydova.booksmart.domain.usecase.books_local

import javax.inject.Inject

data class BooksLocalUseCase (
    val getLocalBookByIdUseCase: GetLocalBookByIdUseCase,
    val insertBookUseCase: InsertBookUseCase,
    val deleteLocalBookUseCase: DeleteLocalBookUseCase,
    val getLocalBooksUseCase: GetLocalBooksUseCase
)
