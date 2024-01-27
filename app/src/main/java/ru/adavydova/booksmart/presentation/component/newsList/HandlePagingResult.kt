package ru.adavydova.booksmart.presentation.component.newsList

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import ru.adavydova.booksmart.domain.model.Book

@Composable
fun handlePagingResult(
    books: LazyPagingItems<Book>
): PagingStateLoad {
    val loadState = books.loadState
    val error: LoadState.Error? = when{

        loadState.refresh is LoadState.Error -> {
            (loadState.refresh as LoadState.Error)
        }
        loadState.append is LoadState.Error -> {
            (loadState.append as LoadState.Error)
        }
        loadState.prepend is LoadState.Error -> {
            (loadState.prepend as LoadState.Error)
        }
        else -> null
    }
    return when{

        loadState.refresh is LoadState.Loading -> {
            PagingStateLoad.Load
        }
        error != null -> {
            PagingStateLoad.Error(error)
        }
        else -> {
           PagingStateLoad.Success
        }
    }



}







sealed class PagingStateLoad{

    object Load: PagingStateLoad()
    class Error(val message: LoadState.Error): PagingStateLoad()
    object NoItemWasFound: PagingStateLoad()
    object Success: PagingStateLoad()
}