package ru.adavydova.booksmart.domain.usecase.readium_books

import android.util.Log
import ru.adavydova.booksmart.domain.repository.ReadiumBooksRepository
import javax.inject.Inject


class UpdateBookOrderByTimeOpeningUseCase @Inject constructor(
    private val repository: ReadiumBooksRepository
) {
    suspend operator fun invoke(progress:String, bookId:Long, time:Long){
        Log.d("P", progress.toString())
        val progress1 = progress.toString()
        Log.d("P", progress1.toFloat().toString())
        Log.d("P", bookId.toString())
        Log.d("P", time.toString())
        repository.updateBookOrderByTimeOpening(
            bookId = bookId,
            progress = progress,
            time = time
        )
    }
}