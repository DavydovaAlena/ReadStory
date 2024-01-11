package ru.adavydova.booksmart.presentation.component.newsList

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import ru.adavydova.booksmart.domain.model.Book
import ru.adavydova.booksmart.presentation.component.search_item.short_variant.SearchItem

@Composable
fun ListOfSearchItems(
    modifier: Modifier = Modifier,
    books: LazyPagingItems<Book>,
    changeErrorState: (LoadState.Error?)-> Unit,
    onClick: (Book)-> Unit
) {

    val handlePagingResult = handlePagingResult(books = books)
    val context = LocalContext.current

    when(handlePagingResult){
        is PagingStateLoad.Error -> {
            changeErrorState(handlePagingResult.message)
        }
        PagingStateLoad.Load -> {
            changeErrorState(null)
            ShimmerListOfSearchItems(modifier)
        }
        PagingStateLoad.NoItemWasFound -> {
            changeErrorState(null)
            NotFoundScreen()
        }
        PagingStateLoad.Success -> {

            changeErrorState(null)

            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
            ) {

                items(books.itemCount) {
                    books[it]?.let { book ->

                        SearchItem(
                            book = book,
                            onClick = onClick)

                    }
                }

            }

        }
    }



}

