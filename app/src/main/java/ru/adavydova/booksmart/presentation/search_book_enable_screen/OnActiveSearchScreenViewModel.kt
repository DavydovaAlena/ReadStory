package ru.adavydova.booksmart.presentation.search_book_enable_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.adavydova.booksmart.domain.usecase.BooksUseCase
import javax.inject.Inject

@HiltViewModel
class OnActiveSearchScreenViewModel @Inject constructor(
    private val bookUseCase: BooksUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _searchBooksState = MutableStateFlow(SearchBooksState())
    val searchBooksState = _searchBooksState.asStateFlow()


    init {
        savedStateHandle.get<String>("query")?.let {
            _searchBooksState.value = searchBooksState.value.copy(
                query = it
            )
        }
    }

    fun onEvent(event: SearchBookEvent) {
        when (event) {

            is SearchBookEvent.UpdateAndSearchQuery -> {
                _searchBooksState.value = searchBooksState.value.copy(
                    query = event.query
                )

                val books = bookUseCase.searchBookUseCase(
                    query = searchBooksState.value.query,
                    maxResults = 20
                ).cachedIn(viewModelScope)

                _searchBooksState.value = searchBooksState.value.copy(
                    books = books
                )

            }

        }
    }
}