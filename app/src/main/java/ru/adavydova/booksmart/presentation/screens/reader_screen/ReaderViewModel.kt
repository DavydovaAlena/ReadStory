package ru.adavydova.booksmart.presentation.screens.reader_screen

import androidx.annotation.ColorInt
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.parcelize.Parcelize
import org.readium.r2.navigator.Decoration
import org.readium.r2.navigator.ExperimentalDecorator
import org.readium.r2.shared.ExperimentalReadiumApi
import org.readium.r2.shared.publication.Locator
import org.readium.r2.shared.publication.services.search.SearchIterator
import org.readium.r2.shared.util.Try
import ru.adavydova.booksmart.domain.model.readium_book.Bookmark
import ru.adavydova.booksmart.domain.model.readium_book.Highlight
import ru.adavydova.booksmart.domain.repository.ReaderRepository
import ru.adavydova.booksmart.domain.usecase.readium_books.ReadiumBookUseCase
import ru.adavydova.booksmart.reader.DummyReaderInitData
import ru.adavydova.booksmart.reader.ReaderInitData
import ru.adavydova.booksmart.util.Resource
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ReaderViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val readerRepository: ReaderRepository,
    private val useCase: ReadiumBookUseCase
) : ViewModel() {

    private val _readerBookState = MutableStateFlow<BookState>(BookState())
    val readerBookState = _readerBookState.asStateFlow()

    @OptIn(ExperimentalReadiumApi::class)
    private val _searchState = MutableStateFlow<SearchState>(SearchState())
    val searchState = _searchState.asStateFlow()

    init {
        val bookId = savedStateHandle.get<Long>("bookId")
        viewModelScope.launch {
            val readerInitData = try {
                checkNotNull(openPublication(bookId!!))

            } catch (e: Exception) {
                DummyReaderInitData(bookId!!)
            }
            _readerBookState.update {
                it.copy(
                    readerInitData = readerInitData,
                    userPrefrences = UserPreferences(readerInitData)
                )
            }

        }

    }

    val stateSetting = MutableStateFlow<Boolean>(false)

    private val _snackbarState = MutableStateFlow(SnackbarState())
    val snackbarState = _snackbarState.asStateFlow()


//    private val searchState = MutableStateFlow<SearchState>(SearchState())
//    private val _searchLocators = MutableStateFlow<List<Locator>>(emptyList())
//    val searchLocator = _searchLocators.asStateFlow()


    private val highlights by lazy {
        _readerBookState.value.readerInitData?.let {
            useCase.getHighlightsForBookUseCase(it.bookId)
        }
    }
    private val activeHighlightId = MutableStateFlow<Long?>(null)


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

    @OptIn(ExperimentalDecorator::class)
    val highlightDecorations by lazy {
        highlights?.combine(activeHighlightId) { highlights, activeId ->
            highlights.flatMap { highlight ->
                highlight.toDecorations(isActive = (highlight.id == activeId))
            }
        }
    }

    @OptIn(ExperimentalDecorator::class)
    private fun Highlight.toDecorations(isActive: Boolean): List<Decoration> {
        fun createDecoration(idSufix: String, style: Decoration.Style) = Decoration(
            id = "$id-$idSufix",
            locator = locator,
            style = style,
            extras = mapOf(
                "id" to id
            )
        )
        return listOfNotNull(
            createDecoration(
                idSufix = "highlight",
                style = when (style) {
                    Highlight.Style.HIGHLIGHT -> Decoration.Style.Highlight(
                        tint = tint,
                        isActive = isActive
                    )

                    Highlight.Style.UNDERLINE -> Decoration.Style.Underline(
                        tint, isActive
                    )
                }
            ),
            annotation.takeIf { it.isNotEmpty() }?.let {
                createDecoration(
                    idSufix = "annotation",
                    style = DecorationStyleAnnotationMark(tint = tint)
                )
            }
        )
    }

    private fun showSnackbarEvent(event: SnackbarEvent) {
        when (event) {
            is SnackbarEvent.ShowSnackbar -> {
                _snackbarState.value = _snackbarState.value.copy(
                    message = event.message,
                )
                viewModelScope.launch(Dispatchers.IO) {
                    delay(4000)
                }
                _snackbarState.value = _snackbarState.value.copy(
                    message = null,
                )
            }
        }
    }

    fun onEvent(event: ReaderEvent) {
        when (event) {


            is ReaderEvent.DeleteBookmaker -> {
                viewModelScope.launch(Dispatchers.IO) {
                    useCase.deleteBookmarkUseCase(event.id)
                }
            }

            ReaderEvent.GetBookmarks -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _readerBookState.value.readerInitData?.let { initData ->
                        when (val result = useCase.getBookmarksForBookUseCase(initData.bookId)) {
                            is Resource.Error -> {
                                showSnackbarEvent(SnackbarEvent.ShowSnackbar(result.message))
                            }

                            is Resource.Success -> {
                                result.data?.collectLatest {
                                    _readerBookState.update { bookState ->
                                        bookState.copy(bookmarks = it)
                                    }
                                }
                            }
                        }
                    }
                }
            }


            is ReaderEvent.InsertBookmark -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _readerBookState.value.readerInitData?.let { readerInitData ->
                        val result = useCase.insertBookmarkUseCase(
                            bookId = readerInitData.bookId,
                            publication = readerInitData.publication,
                            locator = event.locator
                        )
                        when (result) {
                            is Resource.Error -> {
                                showSnackbarEvent(SnackbarEvent.ShowSnackbar(result.message))
                            }

                            is Resource.Success -> {
                                showSnackbarEvent(SnackbarEvent.ShowSnackbar("The bookmark has been created"))
                            }
                        }
                    }

                }
            }

            is ReaderEvent.SaveProgression -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _readerBookState.value.readerInitData?.let { readerInitData ->
                        Timber.v("Saving locator for book ${readerInitData.bookId}: ${event.locator}.")
                        useCase.saveProgressionUseCase(event.locator, readerInitData.bookId)
                    }
                }
            }

            is ReaderEvent.DeleteHighlight -> {
                viewModelScope.launch(Dispatchers.IO) {
                    when (val result = useCase.deleteHighlightUseCase(event.id)) {
                        is Resource.Error -> {
                            showSnackbarEvent(SnackbarEvent.ShowSnackbar(result.message))
                        }

                        is Resource.Success -> {
                            showSnackbarEvent(SnackbarEvent.ShowSnackbar("The highlight has been deleted"))
                        }
                    }
                }
            }

            is ReaderEvent.HighlightById -> {
                viewModelScope.launch(Dispatchers.IO) {
                    useCase.getHighlightByIdUseCase(event.id)
                }
            }

            is ReaderEvent.UpdateHighlightAnnotation -> {
                viewModelScope.launch(Dispatchers.IO) {
                    useCase.updateHighlightAnnotationUseCase(event.id, event.annotation)
                }
            }

            is ReaderEvent.UpdateHighlightStyle -> {
                viewModelScope.launch(Dispatchers.IO) {
                    useCase.updateHighlightAnnotationStyleUseCase(
                        highlightId = event.id,
                        style = event.style,
                        tint = event.tint
                    )
                }
            }

            is ReaderEvent.Search -> {
               _searchState.update { it.copy(visible = true) }
            }

            ReaderEvent.OpenOutlineRequest -> {

            }

            ReaderEvent.OpenCloseSettingBottomSheet -> {
                stateSetting.value = !stateSetting.value
            }

            is ReaderEvent.CloseReader -> {
                viewModelScope.launch(Dispatchers.IO) {
                    useCase.updateBookOrderByTimeOpeningUseCase(
                        bookId = event.bookId,
                        progress = event.progress,
                        time = event.time)
                }
            }
        }

    }


}


data class BookState(
    val readerInitData: ReaderInitData? = null,
    val userPrefrences: UserPreferences<*, *>? = null,
    val activeHighlight: Highlight? = null,
    val highlights: List<Highlight> = emptyList(),
    val bookmarks: List<Bookmark> = emptyList()
)

data class SnackbarState(
    val message: String? = null,
)

data class SearchState @OptIn(ExperimentalReadiumApi::class) constructor(
    val visible: Boolean? = false,
    val searchIterator: SearchIterator? = null,
    val searchQuery: String? = null
)

sealed class ReaderEvent {
    data class CloseReader(val bookId: Long, val progress: String, val time:Long): ReaderEvent()
    object OpenCloseSettingBottomSheet : ReaderEvent()
    object OpenOutlineRequest : ReaderEvent()
    class Search(val query: String) : ReaderEvent()
    class HighlightById(val id: Long) : ReaderEvent()
    data class UpdateHighlightAnnotation(val id: Long, val annotation: String) : ReaderEvent()
    data class UpdateHighlightStyle(
        val id: Long,
        val style: Highlight.Style,
        @ColorInt val tint: Int
    ) : ReaderEvent()

    class DeleteHighlight(val id: Long) : ReaderEvent()
    data class SaveProgression(val locator: Locator) : ReaderEvent()
    class InsertBookmark(val locator: Locator) : ReaderEvent()
    class DeleteBookmaker(val id: Long) : ReaderEvent()
    object GetBookmarks : ReaderEvent()


}

sealed class SnackbarEvent {
    class ShowSnackbar(val message: String) : SnackbarEvent()
}

/**
 * Decoration Style for a page margin icon.
 *
 * This is an example of a custom Decoration Style declaration.
 */
@Parcelize
@OptIn(ExperimentalDecorator::class)
data class DecorationStyleAnnotationMark(@ColorInt val tint: Int) : Decoration.Style

/**
 * Decoration Style for a page number label.
 *
 * This is an example of a custom Decoration Style declaration.
 *
 * @param label Page number label as declared in the `page-list` link object.
 */
@Parcelize
@OptIn(ExperimentalDecorator::class)
data class DecorationStylePageNumber(val label: String) : Decoration.Style
