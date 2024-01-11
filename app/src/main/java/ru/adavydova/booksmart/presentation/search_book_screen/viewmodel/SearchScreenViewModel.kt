package ru.adavydova.booksmart.presentation.search_book_screen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.adavydova.booksmart.domain.usecase.BooksUseCase
import ru.adavydova.booksmart.presentation.search_book_screen.event.SearchBookEvent
import ru.adavydova.booksmart.presentation.search_book_screen.state.SearchBooksState
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val bookUseCase: BooksUseCase
) : ViewModel() {

    private val _searchBooksState = MutableStateFlow(SearchBooksState())
    val searchBooksState = _searchBooksState.asStateFlow()


    fun onEvent(event: SearchBookEvent) {
        when (event) {

            is SearchBookEvent.UpdateAndSearchQuery -> {
                _searchBooksState.value = searchBooksState.value.copy(
                    query = event.query
                )
                Log.d("Q", searchBooksState.value.query)

                val books = bookUseCase.searchBookUseCase(
                    searchBooksState.value.query
                ).cachedIn(viewModelScope)

                _searchBooksState.value = searchBooksState.value.copy(
                    books = books
                )

            }

        }
    }
}