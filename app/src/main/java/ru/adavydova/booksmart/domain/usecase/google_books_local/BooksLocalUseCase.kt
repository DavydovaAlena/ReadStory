package ru.adavydova.booksmart.domain.usecase.google_books_local

data class BooksLocalUseCase (
    val getLocalBookByIdUseCase: GetLocalBookByIdUseCase,
    val insertBookUseCase: InsertBookUseCase,
    val deleteLocalBookUseCase: DeleteLocalBookUseCase,
    val getLocalBooksUseCase: GetLocalBooksUseCase
)
