package ru.adavydova.booksmart.presentation.component.newsList

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import ru.adavydova.booksmart.domain.model.Book
import ru.adavydova.booksmart.presentation.component.search_item.middle_variant.ShimmerCardBookItem
import ru.adavydova.booksmart.presentation.component.search_item.short_variant.ShimmerSearchItem
import ru.adavydova.booksmart.presentation.screens.inactive_search_book_screen.component.MAX_TOOLBAR_HEIGHT
import ru.adavydova.booksmart.presentation.screens.inactive_search_book_screen.component.MIN_TOOLBAR_HEIGHT
import ru.adavydova.booksmart.presentation.screens.inactive_search_book_screen.component.MiExitUntilCollapsedState

@Composable
fun ListOfSearchItems(
    modifier: Modifier = Modifier,
    books: LazyPagingItems<Book>,
    card: @Composable (Book, (String)->Unit)-> Unit,
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
    books: LazyPagingItems<Book>,
    card: @Composable (Book, (Book)->Unit)-> Unit,
    changeErrorState: (LoadState.Error?)-> Unit,
    navigateToDetail: (Book)-> Unit,
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

