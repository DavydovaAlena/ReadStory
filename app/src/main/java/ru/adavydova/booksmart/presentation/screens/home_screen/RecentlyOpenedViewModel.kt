package ru.adavydova.booksmart.presentation.screens.home_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.adavydova.booksmart.domain.model.readium_book.ReadiumBook
import ru.adavydova.booksmart.domain.model.readium_book.TemporaryOpeningBook
import ru.adavydova.booksmart.domain.usecase.readium_books.ReadiumBookUseCase
import javax.inject.Inject

@HiltViewModel
class RecentlyOpenedViewModel @Inject constructor(
    private val useCase: ReadiumBookUseCase
):ViewModel() {

    private val _recentlyOpenedBook = MutableStateFlow(RecentlyOpenedBooksState())
    val recentlyOpenedBook = _recentlyOpenedBook.asStateFlow()


    init {
        onEvent(RecentlyOpenedBooksEvent.GetRecentlyOpenedBooks)
    }

    fun onEvent(event: RecentlyOpenedBooksEvent){
        when(event){
            RecentlyOpenedBooksEvent.GetRecentlyOpenedBooks -> {
                _recentlyOpenedBook.update { it.copy(load = true) }
                viewModelScope.launch(Dispatchers.IO) {
                    useCase.getBooksOrderByTimeOpeningUseCase()
                        .collectLatest {books->
                            _recentlyOpenedBook.update {
                                it.copy(books = books, load = false) }
                        }
                }
            }
        }
    }



}

sealed class RecentlyOpenedBooksEvent{
    object GetRecentlyOpenedBooks: RecentlyOpenedBooksEvent()
}

data class RecentlyOpenedBooksState(
    val load: Boolean = false,
    val error: String? = null,
    val books: List<TemporaryOpeningBook> = emptyList()
)