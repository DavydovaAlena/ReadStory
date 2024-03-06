package ru.adavydova.booksmart.presentation.screens.bookshelf_screen

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.adavydova.booksmart.domain.Bookshelf
import ru.adavydova.booksmart.domain.model.readium_book.ReadiumBook
import ru.adavydova.booksmart.domain.repository.ReaderRepository
import ru.adavydova.booksmart.domain.usecase.readium_books.ReadiumBookUseCase
import ru.adavydova.booksmart.util.Resource
import ru.adavydova.booksmart.util.tryOrLog
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class BookshelfViewModel @Inject constructor(
    private val useCase: ReadiumBookUseCase,
    private val bookshelf: Bookshelf,
    private val readerRepository: ReaderRepository
) : ViewModel() {


    private val _statePublication = MutableStateFlow(BookshelfState())
    val statePublication = _statePublication.asStateFlow()

    val bookshelfState = bookshelf.bookshelfState
    val editMoreState = MutableStateFlow(EditModeState())

    private val _errorPublicationState = MutableStateFlow(SnackbarState())
    val errorPublicationState = _errorPublicationState.asStateFlow()

    val openBookChannel = Channel<OpenBookEvent>()

    init {
        onEvent(BookshelfScreenEvent.GetBooks)
    }

    fun onEvent(event: BookshelfScreenEvent) {

        when (event) {

            BookshelfScreenEvent.CloseEditingMode -> {
                editMoreState.value = editMoreState.value.copy(
                    editMode = EditMoreEvent.EditingModeIsDisable,
                    selectedItems = emptyList()
                )
            }

            is BookshelfScreenEvent.LongPressOnTheBook -> {

                val oldSelected = editMoreState.value.selectedItems.toMutableList()
                val newSelected: List<ReadiumBook> =
                    if (oldSelected.contains(event.book)) oldSelected.minus(event.book) else oldSelected.plus(
                        event.book
                    )
                editMoreState.value = editMoreState.value.copy(
                    editMode = EditMoreEvent.EditingModeIsEnabled,
                    selectedItems = newSelected
                )
            }

            is BookshelfScreenEvent.ErrorPublication -> {
                Timber.e("BookshelfVM",event.error)
                viewModelScope.launch(Dispatchers.IO) {
                    _errorPublicationState.update {
                        it.copy(
                            showSnackbar = true,
                            message = event.error
                        )
                    }
                    delay(4000)
                    _errorPublicationState.update { it.copy(showSnackbar = false, message = null) }
                }
            }

            BookshelfScreenEvent.SuccessPublication -> {
                onEvent(BookshelfScreenEvent.GetBooks)
            }

            is BookshelfScreenEvent.DeletePublication -> {
                _statePublication.update { it.copy(load = true) }
                event.books.forEach { book ->
                    viewModelScope.launch(Dispatchers.IO) {
                        val id = book.id!!
                        useCase.deleteBookUseCase(id)
                        tryOrLog { book.url.toFile()?.delete() }
                        tryOrLog { File(book.cover).delete() }
                         useCase.deleteTimeOpeningBookUseCase(id)
                    }
                }
                editMoreState.update { it.copy(editMode = EditMoreEvent.EditingModeIsDisable, selectedItems = emptyList()) }
                _statePublication.update { it.copy(load = false) }
            }

            BookshelfScreenEvent.GetBooks -> {
                _statePublication.update { it.copy(load = true) }
                viewModelScope.launch(Dispatchers.IO) {
                    when (val result = useCase.getAllBooksUseCase()) {
                        is Resource.Error -> {
                            _statePublication.update { bookshelfState ->
                                bookshelfState.copy(error = result.message)
                            }
                        }

                        is Resource.Success -> {
                            result.data?.collect {
                                _statePublication.update { bookshelfState ->
                                    bookshelfState.copy(books = it)
                                }
                            }
                        }
                    }

                }
                _statePublication.update { it.copy(load = false) }
            }

            is BookshelfScreenEvent.ImportPublicationFromStorage -> {
                _statePublication.update { it.copy(load = true) }
                bookshelf.importPublicationFromStorage(event.uri)
                _statePublication.update { it.copy(load = false) }

            }

            is BookshelfScreenEvent.OpenPublication -> {
                viewModelScope.launch {
                    readerRepository.open(event.bookId)
                        .onFailure {
                            onEvent(BookshelfScreenEvent.ErrorPublication(it.message))
                        }
                        .onSuccess {
                            openBookChannel.send(OpenBookEvent.OpenBook)
                        }
                }
            }
        }
    }


}

sealed class EditMoreEvent {

    operator fun invoke(): Boolean {
        return when (this) {
            EditingModeIsDisable -> false
            EditingModeIsEnabled -> true
        }
    }

    object EditingModeIsEnabled : EditMoreEvent()
    object EditingModeIsDisable : EditMoreEvent()
}

sealed class OpenBookEvent {
    object OpenBook : OpenBookEvent()
}

sealed class BookshelfScreenEvent {

    object CloseEditingMode : BookshelfScreenEvent()
    class DeletePublication(val books: List<ReadiumBook>) : BookshelfScreenEvent()
    class ImportPublicationFromStorage(val uri: Uri) : BookshelfScreenEvent()
    class LongPressOnTheBook(val book: ReadiumBook) : BookshelfScreenEvent()
    class ErrorPublication(val error: String) : BookshelfScreenEvent()
    object SuccessPublication : BookshelfScreenEvent()
    class OpenPublication(val bookId: Long) : BookshelfScreenEvent()
    object GetBooks : BookshelfScreenEvent()
}

data class EditModeState(
    val editMode: EditMoreEvent = EditMoreEvent.EditingModeIsDisable,
    val selectedItems: List<ReadiumBook> = emptyList()
)

data class SnackbarState(
    val message: String? = null,
    val showSnackbar: Boolean = false
)

data class BookshelfState(
    val books: List<ReadiumBook> = emptyList(),
    val load: Boolean = false,
    val error: String? = null
)