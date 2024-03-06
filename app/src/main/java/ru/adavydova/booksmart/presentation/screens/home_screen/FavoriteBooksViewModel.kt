package ru.adavydova.booksmart.presentation.screens.home_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.adavydova.booksmart.domain.model.google_book.GoogleBook
import ru.adavydova.booksmart.domain.model.readium_book.ReadiumBook
import ru.adavydova.booksmart.domain.usecase.google_books_local.BooksLocalUseCase
import ru.adavydova.booksmart.domain.usecase.readium_books.ReadiumBookUseCase
import ru.adavydova.booksmart.util.Resource
import javax.inject.Inject

@HiltViewModel
class FavoriteBooksViewModel @Inject constructor(
    private val booksLocalUseCase: BooksLocalUseCase,
) : ViewModel() {

    private val _favoriteBooksState = MutableStateFlow(FavoriteBooksState())
    val favoriteBooksState = _favoriteBooksState.asStateFlow()


    init {
        _favoriteBooksState.value = favoriteBooksState.value.copy(load = true)
        viewModelScope.launch {
            when (val result = booksLocalUseCase.getLocalBooksUseCase()) {
                is Resource.Error -> {
                    _favoriteBooksState.update { state ->
                        state.copy(error = result.message)
                    }
                }

                is Resource.Success -> {
                    result.data?.onEach { books ->
                        _favoriteBooksState.update { state ->
                            state.copy(googleBooks = books)
                        }
                    }?.launchIn(viewModelScope)
                }
            }
        }
        _favoriteBooksState.value = favoriteBooksState.value.copy(load = false)
    }
}

data class FavoriteBooksState(
    val load: Boolean = false,
    val error: String? = null,
    val googleBooks: List<GoogleBook> = emptyList()
)
