package ru.adavydova.booksmart.domain.repository

import org.readium.r2.shared.util.Try
import ru.adavydova.booksmart.reader.OpeningError
import ru.adavydova.booksmart.reader.ReaderInitData


interface ReaderRepository {
    suspend fun open(bookId: Long): Try<ReaderInitData, OpeningError>
}