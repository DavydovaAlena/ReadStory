package ru.adavydova.booksmart.presentation.inactive_search_book_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.adavydova.booksmart.domain.usecase.BooksUseCase
import javax.inject.Inject

@HiltViewModel
class InactiveSearchBookScreenViewModel
    @Inject constructor(
        savedStateHandle: SavedStateHandle,
        private val useCase: BooksUseCase
    ): ViewModel() {

   private val _screenState = MutableStateFlow(InactiveSearchBookScreenState())
   val screenState = _screenState.asStateFlow()

   init {

      savedStateHandle.get<String>("query")?.let { query ->
          viewModelScope.launch {
              val books = searchBooks(query)
              _screenState.value = screenState.value.copy(
                  query = query,
                  books = books
              )
          }
      }
   }
    private suspend fun searchBooks(query:String) = withContext(Dispatchers.IO){
        val books = useCase.searchBookUseCase(
            query = query,
            maxResults = 10
        ).cachedIn(viewModelScope)
        books
    }


    fun onEvent(event: InactiveSearchScreenEvent) {
        when (event) {
            is InactiveSearchScreenEvent.AddToDeferred -> TODO()
            is InactiveSearchScreenEvent.AddToFavorites -> TODO()
            InactiveSearchScreenEvent.ApplyAFilter -> TODO()
            is InactiveSearchScreenEvent.SelectFilter -> TODO()
            is InactiveSearchScreenEvent.SelectLanguageBook -> TODO()
            is InactiveSearchScreenEvent.SelectOrderType -> TODO()
            InactiveSearchScreenEvent.ShowAdditionalParameter -> TODO()
            InactiveSearchScreenEvent.SideBarUse -> TODO()

        }
    }

}