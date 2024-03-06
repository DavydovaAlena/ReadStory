package ru.adavydova.booksmart.presentation.screens.search_book_enable_screen

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.update
import ru.adavydova.booksmart.domain.usecase.google_books_remote.BooksRemoteUseCase
import javax.inject.Inject

@HiltViewModel
class OnActiveSearchScreenViewModel @Inject constructor(
    private val bookUseCase: BooksRemoteUseCase,
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

                searchBooksState.value.books?.cancellable()
                _searchBooksState.update { it.copy(query = event.query) }

                val books = bookUseCase.searchBookUseCase(
                    query = searchBooksState.value.query,
                    maxResults = 10
                ).cachedIn(viewModelScope)

                _searchBooksState.value = searchBooksState.value.copy(
                    books = books
                )

            }

             SearchBookEvent.ClearQuery -> {
                _searchBooksState.value = searchBooksState.value.copy(
                    query = "",
                )
                 val books = bookUseCase.searchBookUseCase(
                     query = searchBooksState.value.query,
                     maxResults = 10
                 ).cachedIn(viewModelScope)

                 _searchBooksState.value = searchBooksState.value.copy(
                     books = books
                 )
            }
        }
    }
}