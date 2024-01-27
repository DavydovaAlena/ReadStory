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
import ru.adavydova.booksmart.presentation.inactive_search_book_screen.filters.FilterBooks
import ru.adavydova.booksmart.presentation.inactive_search_book_screen.filters.FilterTypeBook
import ru.adavydova.booksmart.presentation.inactive_search_book_screen.filters.LanguageRestrictFilterBooks
import ru.adavydova.booksmart.presentation.inactive_search_book_screen.filters.OrderBooks
import ru.adavydova.booksmart.presentation.inactive_search_book_screen.filters.selectFilterType
import ru.adavydova.booksmart.presentation.inactive_search_book_screen.filters.selectLanguageRestrict
import ru.adavydova.booksmart.presentation.inactive_search_book_screen.filters.selectOrderType
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

    val filters = mapOf(
        FilterBooks::class to listOf(FilterBooks.Partial.filter, FilterBooks.Full.filter, FilterBooks.PaidEbooks.filter, FilterBooks.Ebooks.filter, FilterBooks.FreeEbooks.filter),
        OrderBooks::class to listOf(OrderBooks.NewestOrderType.order, OrderBooks.RelevanceOrderType.order),
        LanguageRestrictFilterBooks::class to listOf(LanguageRestrictFilterBooks.DeBooks.language, LanguageRestrictFilterBooks.FrBooks.language,
            LanguageRestrictFilterBooks.EnBooks.language, LanguageRestrictFilterBooks.RuBooks.language, LanguageRestrictFilterBooks.AllLanguage.language)
    )


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
                when(event.filterType){
                    OrderBooks::class ->  _screenState.value = screenState.value.copy(showOrderMenu = event.enable)
                    FilterBooks::class -> _screenState.value = screenState.value.copy(showFilterMenu = event.enable)
                    LanguageRestrictFilterBooks::class ->_screenState.value = screenState.value.copy(showLanguageMenu = event.enable)
                }

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
                filters.forEach { (typeFilter, listFilter) ->
                    val filter = listFilter.firstOrNull { it ==  event.filter}
                    filter?.let {
                        when(typeFilter){
                            OrderBooks::class -> _stateFilterBook.value = stateFilterBook.value.copy(orderType = it.selectOrderType())
                            FilterBooks::class -> _stateFilterBook.value = stateFilterBook.value.copy(filter = it.selectFilterType())
                            LanguageRestrictFilterBooks::class -> _stateFilterBook.value = stateFilterBook.value.copy(languageRestrict = it.selectLanguageRestrict())
                        }
                    }
                }

            }

        }
    }

}

