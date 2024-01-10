package ru.adavydova.booksmart.presentation.search_book_screen.viewmodel

import android.app.Activity
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.adavydova.booksmart.domain.usecase.BooksUseCase
import ru.adavydova.booksmart.permission_logic.PermissionTextProvider
import ru.adavydova.booksmart.presentation.search_book_screen.event.SearchBookEvent
import ru.adavydova.booksmart.presentation.search_book_screen.state.SearchBooksState
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val bookUseCase: BooksUseCase
): ViewModel() {

    private val _searchBooksState = MutableStateFlow(SearchBooksState())
    val searchBooksState = _searchBooksState.asStateFlow()


    fun onEvent(event: SearchBookEvent){
        when(event){

            SearchBookEvent.SearchNews -> {
                val books = bookUseCase.searchBookUseCase(
                    searchBooksState.value.query
                ).cachedIn(viewModelScope)
                _searchBooksState.value = searchBooksState.value.copy(
                    books = books
                )

            }
            is SearchBookEvent.UpdateQuery -> {
                _searchBooksState.value = searchBooksState.value.copy(
                    query = event.query
                )
                val books = bookUseCase.searchBookUseCase(
                    searchBooksState.value.query
                ).cachedIn(viewModelScope)

                _searchBooksState.value = searchBooksState.value.copy(
                    books = books
                )

            }

            is SearchBookEvent.GoogleAssistantUse -> {

                _searchBooksState.value = searchBooksState.value.copy(
                    query = event.query
                )
            }
        }
    }
}