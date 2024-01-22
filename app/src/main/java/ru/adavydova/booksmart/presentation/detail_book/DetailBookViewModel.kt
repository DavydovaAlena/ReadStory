package ru.adavydova.booksmart.presentation.detail_book

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.adavydova.booksmart.data.remote.util.Resource
import ru.adavydova.booksmart.domain.usecase.BooksUseCase
import javax.inject.Inject

@HiltViewModel
class DetailBookViewModel @Inject constructor(
    private val useCase: BooksUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _bookState = MutableStateFlow(DetailBookState())
    val bookState = _bookState.asStateFlow()

    init {
        savedStateHandle.get<String>("bookId")?.let {bookId->
            viewModelScope.launch(Dispatchers.IO) {
                val book = useCase.getBookByIdUseCase(bookId)
                when (book){
                    is Resource.Error -> {
                        _bookState.value = bookState.value.copy(
                           error = book.message
                        )
                    }
                    is Resource.Success -> {
                        _bookState.value = bookState.value.copy(
                            book = book.data,
                            error = null
                        )
                    }
                }

            }

        }
    }


}