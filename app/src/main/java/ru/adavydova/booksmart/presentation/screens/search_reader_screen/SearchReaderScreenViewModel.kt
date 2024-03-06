package ru.adavydova.booksmart.presentation.screens.search_reader_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.InvalidatingPagingSourceFactory
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.readium.r2.shared.ExperimentalReadiumApi
import org.readium.r2.shared.publication.Locator
import org.readium.r2.shared.publication.LocatorCollection
import org.readium.r2.shared.publication.Publication
import org.readium.r2.shared.publication.services.search.SearchIterator
import org.readium.r2.shared.publication.services.search.SearchTry
import org.readium.r2.shared.publication.services.search.search
import org.readium.r2.shared.util.Try
import ru.adavydova.booksmart.R
import ru.adavydova.booksmart.domain.repository.ReaderRepository
import ru.adavydova.booksmart.presentation.screens.reader_screen.common.ReaderCommand
import ru.adavydova.booksmart.reader.DummyReaderInitData
import ru.adavydova.booksmart.reader.ReaderInitData
import ru.adavydova.booksmart.util.UserError
import javax.inject.Inject

@HiltViewModel
class SearchReaderScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val readerRepository: ReaderRepository
) : ViewModel() {

    private val _searchState = MutableStateFlow(SearchReaderScreenState())
    private val publication: Publication?
        get() = _searchState.value.readerInitData?.publication

    private val _startNewSearch = MutableSharedFlow<SearchCommand>()
    val startNewSearch = _startNewSearch.asSharedFlow()

    private val _searchLocators = MutableStateFlow<List<Locator>>(emptyList())
    private val pagingSourceFactory = InvalidatingPagingSourceFactory {
        SearchPagingSource(listener = PagingSourceListener())
    }


    @OptIn(ExperimentalReadiumApi::class)
    private var searchIterator: SearchIterator? = null

    init {
        val bookId = savedStateHandle.get<Long>("bookId")
        viewModelScope.launch {
            val readerInitData = try {
                checkNotNull(openPublication(bookId!!))

            } catch (e: Exception) {
                DummyReaderInitData(bookId!!)
            }
            _searchState.update { it.copy(readerInitData = readerInitData) }
        }
    }

    private inner class PagingSourceListener: SearchPagingSource.Listener{
        @OptIn(ExperimentalReadiumApi::class)
        override suspend fun next(): SearchTry<LocatorCollection?> {
            val iterator = searchIterator?: return Try.success(null)
            return iterator.next().onSuccess {
                _searchLocators.value += (it?.locators?: emptyList())
            }
        }
    }
    val searchResult: Flow<PagingData<Locator>> =
        Pager(PagingConfig(pageSize = 20), pagingSourceFactory = pagingSourceFactory)
            .flow.cachedIn(viewModelScope)

    private suspend fun openPublication(bookId: Long): ReaderInitData? =
        withContext(Dispatchers.Default) {
            when (val result = readerRepository.open(bookId)) {
                is Try.Failure -> {
                    null
                }

                is Try.Success -> {
                    result.value
                }
            }
        }

    @OptIn(ExperimentalReadiumApi::class)
    fun onEvent(event: SearchReaderScreenEvent) {
        when (event) {

            is SearchReaderScreenEvent.Search -> {
                if (event.query == _searchState.value.query) return
                _searchState.update { it.copy(load = true, query = event.query) }
                _searchLocators.value = emptyList()
                viewModelScope.launch(Dispatchers.IO) {
                    searchIterator = publication?.search(event.query)
                        ?: run {
                            _searchState.update {
                                it.copy(
                                    load = false,
                                    error = ReaderCommand.ActivityCommand.ToastError(
                                        error =
                                        UserError(
                                            R.string.search_error_not_searchable,
                                            cause = null
                                        )
                                    )
                                )
                            }
                            null
                        }
                    pagingSourceFactory.invalidate()
                    _startNewSearch.emit(SearchCommand.StartNewSearch)
                }

            }

            SearchReaderScreenEvent.CancelSearch -> {
                viewModelScope.launch {
                   _searchLocators.value = emptyList()
                    searchIterator?.close()
                    searchIterator = null
                    pagingSourceFactory.invalidate()
                }
            }
        }
    }
}

data class SearchReaderScreenState(
    val query: String = "",
    val error: ReaderCommand.ActivityCommand.ToastError? = null,
    val load: Boolean = false,
    val readerInitData: ReaderInitData? = null
)

sealed class SearchCommand {
    object StartNewSearch : SearchCommand()

}

sealed class SearchReaderScreenEvent {
    class Search(val query: String) : SearchReaderScreenEvent()
    object CancelSearch: SearchReaderScreenEvent()
}