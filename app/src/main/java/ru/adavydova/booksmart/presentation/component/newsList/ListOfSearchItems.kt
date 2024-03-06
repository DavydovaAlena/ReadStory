package ru.adavydova.booksmart.presentation.component.newsList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import ru.adavydova.booksmart.domain.model.google_book.GoogleBook
import ru.adavydova.booksmart.presentation.component.search_item.middle_variant.ShimmerCardBookItem
import ru.adavydova.booksmart.presentation.component.search_item.short_variant.ShimmerSearchItem

@Composable
fun ListOfSearchItems(
    modifier: Modifier = Modifier,
    books: LazyPagingItems<GoogleBook>,
    card: @Composable (GoogleBook, (String)->Unit)-> Unit,
    changeErrorState: (LoadState.Error?)-> Unit,
    onClick: (String)-> Unit,
) {

    val handlePagingResult = handlePagingResult(books = books)
    
    when(handlePagingResult){
        is PagingStateLoad.Error -> {
            changeErrorState(handlePagingResult.message)
        }
        PagingStateLoad.Load -> {
            changeErrorState(null)
            ShimmerListOfSearchItems(
                modifier = modifier,
                simmerItem = {
                    ShimmerSearchItem()
                })
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
                        card(book, onClick)
                    }
                }

            }

        }
    }

}

@Composable
fun ListBooksWithScrollState(
    lazyState:LazyListState,
    modifier: Modifier = Modifier,
    books: LazyPagingItems<GoogleBook>,
    card: @Composable (GoogleBook, (GoogleBook)->Unit)-> Unit,
    changeErrorState: (LoadState.Error?)-> Unit,
    navigateToDetail: (GoogleBook)-> Unit,
) {

    val handlePagingResult = handlePagingResult(books = books)




    when(handlePagingResult){
        is PagingStateLoad.Error -> {
            changeErrorState(handlePagingResult.message)
        }
        PagingStateLoad.Load -> {
            changeErrorState(null)
            ShimmerListOfSearchItems(
                modifier = modifier,
                simmerItem = {
                    ShimmerCardBookItem()
                })
        }
        PagingStateLoad.NoItemWasFound -> {
            changeErrorState(null)
            NotFoundScreen()
        }
        PagingStateLoad.Success -> {

            changeErrorState(null)


            LazyColumn(
                state = lazyState,
                verticalArrangement = Arrangement.spacedBy(16.dp) ,
                modifier = modifier
                    .layoutId("books")
                    .fillMaxSize()
            ) {

                items(books.itemCount){
                    val book = books[it]
                    book?.let {
                        card(book, navigateToDetail )
                    }
                }
            }

        }
    }

}

