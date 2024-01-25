package ru.adavydova.booksmart.presentation.inactive_search_book_screen.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import ru.adavydova.booksmart.domain.model.Book
import ru.adavydova.booksmart.presentation.component.newsList.ListBooksWithScrollState
import ru.adavydova.booksmart.presentation.component.search_item.middle_variant.CardBookItem
import ru.adavydova.booksmart.presentation.inactive_search_book_screen.InactiveSearchBookScreenViewModel
import ru.adavydova.booksmart.presentation.main_screen.PermissionTextProvider



object ScrollInactiveScreenValue{
    const val CARD_SIZE = 200

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InactiveSearchBookScreen(
    modifier: Modifier = Modifier,
    navigateToDetailBook: (Book)-> Unit,
    navigateToOnActiveSearchScreen : (String)->Unit,
    navigateToInactiveSearchScreen: (String)->Unit,
    checkingThePermission: ((PermissionTextProvider, Boolean) -> Unit),
    viewModel: InactiveSearchBookScreenViewModel = hiltViewModel<InactiveSearchBookScreenViewModel>(),
) {

    val searchState by viewModel.screenState.collectAsState()
    val books = searchState.books?.collectAsLazyPagingItems()

    val errorState: MutableState<LoadState.Error?> = remember {
        mutableStateOf(null)
    }

    val lazyState = rememberLazyListState()
    val der  = remember { derivedStateOf { lazyState.firstVisibleItemScrollOffset } }
    val der2  = remember { derivedStateOf { lazyState.firstVisibleItemIndex } }


    val toolbarHeightRange = with(LocalDensity.current) {
        MIN_TOOLBAR_HEIGHT.roundToPx()..MAX_TOOLBAR_HEIGHT.roundToPx()
    }
    val toolbarState = rememberSaveable(saver = MiExitUntilCollapsedState.Saver) {
        MiExitUntilCollapsedState(toolbarHeightRange)
    }
    val scrollState = rememberScrollState()



    LaunchedEffect(key1 = der.value, block = {

        if (lazyState.firstVisibleItemIndex<1){
            toolbarState.scrollValue = der.value
        }
    })


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = {

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                SearchBooksMotionHandler(
                    modifier = Modifier
                        .layoutId("books")
                        .padding(it),
                    currentHeight = toolbarState.height.dp,
                    progress = toolbarState.progress,
                    navigateToInactiveSearchScreen = navigateToInactiveSearchScreen,
                    navigateToOnActiveSearchScreen = navigateToOnActiveSearchScreen,
                    query = searchState.query,
                    checkingThePermission = checkingThePermission
                )



                books?.let { pagingBooks ->

                    ListBooksWithScrollState(
                        lazyState = lazyState,
                        books = pagingBooks,
                        modifier = Modifier
                            .layoutId("books")
                            .padding(
                                top = 2.dp,
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 16.dp
                            ),
                        scrollState = scrollState,
                        changeErrorState = { errorState.value = it },
                        navigateToDetail = navigateToDetailBook,
                        card = { book, isClicked ->
                            CardBookItem(book, isClicked)
                        })
                }


            }


        }
    )
}
