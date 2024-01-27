package ru.adavydova.booksmart.presentation.inactive_search_book_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.adavydova.booksmart.domain.usecase.BooksUseCase
import ru.adavydova.booksmart.presentation.inactive_search_book_screen.filters.FilterBookState
import javax.inject.Inject

@HiltViewModel
class InactiveSearchBookScreenViewModel
@Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val useCase: BooksUseCase
) : ViewModel() {

    private val _screenState = MutableStateFlow(InactiveSearchBookScreenState())
    val screenState = _screenState.asStateFlow()


    private val _stateFilterBook = MutableStateFlow<FilterBookState>(
        FilterBookState(
            orderType = _screenState.value.orderType,
            filter = _screenState.value.filter,
            languageRestrict = _screenState.value.languageRestrict
        )
    )
    val stateFilterBook = _stateFilterBook.asStateFlow()


    init {
        savedStateHandle.get<String>("query")?.let { query ->
            viewModelScope.launch {
                _screenState.value = screenState.value.copy(query = query)
                val books = searchBooks()
                _screenState.value = screenState.value.copy(
                    books = books
                )
            }
        }
    }


    private suspend fun searchBooks() = withContext(Dispatchers.IO) {
        val books = useCase.searchBookUseCase(
            query = screenState.value.query,
            maxResults = 5).cachedIn(viewModelScope)
        books
    }


    fun onEvent(event: InactiveSearchScreenEvent) {
        when (event) {
            is InactiveSearchScreenEvent.OpenOrCloseSearchMenu -> {
                _screenState.value = screenState.value.copy(showSearchMenu = event.enable)
            }

            is InactiveSearchScreenEvent.OpenOrCloseFilterMenu -> {
                _screenState.value = screenState.value.copy(showFilterMenu = event.enable)
            }

            is InactiveSearchScreenEvent.OpenOrCloseLanguageMenu -> {
                _screenState.value = screenState.value.copy(showLanguageMenu = event.enable)
            }

            is InactiveSearchScreenEvent.OpenOrCloseOrderMenu -> {
                _screenState.value = screenState.value.copy(showOrderMenu = event.enable)
            }

            InactiveSearchScreenEvent.CancelAndCloseSearchMenu -> {
                _stateFilterBook.value = stateFilterBook.value.copy(
                    orderType = _screenState.value.orderType,
                    filter = _screenState.value.filter,
                    languageRestrict = _screenState.value.languageRestrict
                )
                _screenState.value = screenState.value.copy(
                    showSearchMenu = ShowState.Close
                )
            }

            InactiveSearchScreenEvent.InsertFilter -> {
                _screenState.value = screenState.value.copy(
                    orderType = _stateFilterBook.value.orderType,
                    filter = _stateFilterBook.value.filter,
                    languageRestrict = _stateFilterBook.value.languageRestrict,
                    showSearchMenu = ShowState.Close
                )

                _screenState.value.books?.cancellable()
                viewModelScope.launch {
                    val books = useCase.filterBooksUseCase(
                        filter =  screenState.value.filter,
                        orderType = screenState.value.orderType,
                        languageRestrict = screenState.value.languageRestrict,
                        maxResult = 5,
                        query = screenState.value.query
                    ).cachedIn(viewModelScope)
                    _screenState.value = screenState.value.copy(
                        books = books
                    )
                }
            }

            is InactiveSearchScreenEvent.SelectFilterType -> {
                _stateFilterBook.value = stateFilterBook.value.copy(filter = event.filterBook)
            }

            is InactiveSearchScreenEvent.SelectLanguageType -> {
                _stateFilterBook.value =
                    stateFilterBook.value.copy(languageRestrict = event.language)
            }

            is InactiveSearchScreenEvent.SelectOrderType -> {
                _stateFilterBook.value = stateFilterBook.value.copy(orderType = event.orderBooks)
            }
        }
    }

}

