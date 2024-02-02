package ru.adavydova.booksmart.presentation.screens.detail_book_screen

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.adavydova.booksmart.util.Resource
import ru.adavydova.booksmart.domain.usecase.books_local.BooksLocalUseCase
import ru.adavydova.booksmart.domain.usecase.books_remote.BooksRemoteUseCase
import javax.inject.Inject

@HiltViewModel
class DetailBookViewModel @Inject constructor(
    private val remoteUseCase: BooksRemoteUseCase,
    private val localUseCase: BooksLocalUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _bookState = MutableStateFlow(DetailBookState())
    val bookState = _bookState.asStateFlow()

    init {
        _bookState.value = bookState.value.copy(load = true)
        savedStateHandle.get<String>("bookId")?.let { bookId ->
            viewModelScope.launch() {
                when (val book = localUseCase.getLocalBookByIdUseCase(bookId)) {
                    null -> {
                        getRemoteBookById(bookId)
                    }
                    else -> {
                        _bookState.value = bookState.value.copy(
                            favorite = true,
                            load = false,
                            book = book
                        )
                    }
                }
            }
        }
    }

    private suspend fun getRemoteBookById(id: String) {
        when (val book = remoteUseCase.getBookByIdUseCase(id)) {
            is Resource.Error -> {
                _bookState.value = bookState.value.copy(
                    error = book.message,
                    load = false
                )
            }

            is Resource.Success -> {
                _bookState.value = bookState.value.copy(
                    book = book.data,
                    error = null,
                    load = false
                )
                onEvent(DetailBookEvent.GetLocalBookById)
            }
        }
    }


    fun onEvent(event: DetailBookEvent) {

        bookState.value.book?.let { book ->

            when (event) {

                DetailBookEvent.GetLocalBookById -> {
                    viewModelScope.launch() {

                        when (localUseCase.getLocalBookByIdUseCase(book.id)) {
                            null -> _bookState.value = bookState.value.copy(favorite = false)
                            else -> _bookState.value = bookState.value.copy(favorite = true)
                        }
                    }

                }

                DetailBookEvent.DeleteOrInsertBook -> {

                    when (bookState.value.favorite) {
                        true -> {
                            viewModelScope.launch {
                                try {
                                    localUseCase.deleteLocalBookUseCase(book)
                                    _bookState.value = bookState.value.copy(favorite = false)
                                } catch (e: Exception) {
                                    _bookState.value = bookState.value.copy(error = e.message)
                                }
                            }
                        }

                        false -> {
                            viewModelScope.launch {
                                try {
                                    localUseCase.insertBookUseCase(book)
                                    _bookState.value = bookState.value.copy(favorite = true)
                                } catch (e: Exception) {
                                    _bookState.value = bookState.value.copy(error = e.message)
                                }
                            }
                        }
                    }
                }
            }
        }
    }


}