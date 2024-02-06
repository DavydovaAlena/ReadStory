package ru.adavydova.booksmart.presentation.screens.search_book_enable_screen.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import ru.adavydova.booksmart.presentation.component.newsList.ListOfSearchItems
import ru.adavydova.booksmart.presentation.component.search_bar.OnActiveSearchBar
import ru.adavydova.booksmart.presentation.component.search_item.short_variant.SearchItem
import ru.adavydova.booksmart.presentation.permission_logic.PermissionTextProvider
import ru.adavydova.booksmart.presentation.screens.search_book_enable_screen.OnActiveSearchScreenViewModel
import ru.adavydova.booksmart.presentation.screens.search_book_enable_screen.SearchBookEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFullWindowScreen(
    viewModel: OnActiveSearchScreenViewModel = hiltViewModel<OnActiveSearchScreenViewModel>(),
    backClick: () -> Unit,
    navigateToFullSearchScreen: (String) -> Unit,
    goOnRequest: (String) -> Unit,
    checkingThePermission: (PermissionTextProvider, Boolean) -> Unit,
    useGoogleAssistant: (String) -> Unit
) {

    val searchState by viewModel.searchBooksState.collectAsState()
    val books = searchState.books?.collectAsLazyPagingItems()

    val errorState: MutableState<LoadState.Error?> = remember {
        mutableStateOf(null)
    }

    Scaffold(
        topBar = {
            OnActiveSearchBar(
                query = searchState.query,
                onValueChange = {
                    viewModel.onEvent(SearchBookEvent.UpdateAndSearchQuery(it))
                },
                checkingThePermission = checkingThePermission,
                backClick = backClick,
                goOnRequest = goOnRequest,
                useGoogleAssistant = useGoogleAssistant,
                clearQuery = { viewModel.onEvent(SearchBookEvent.ClearQuery) }

            )
        },

        ) { padding ->

        errorState.value?.error?.printStackTrace()

        books?.let { pagingBooks ->

            ListOfSearchItems(
                books = pagingBooks,
                modifier = Modifier
                    .padding(
                        top = padding.calculateTopPadding(),
                        start = padding.calculateBottomPadding(),
                        end = 16.dp,
                        bottom = padding.calculateBottomPadding()
                    ),
                changeErrorState = { errorState.value = it },
                onClick = { navigateToFullSearchScreen(it) },
                card = { book, onClick ->
                    SearchItem(book = book, onClick = onClick)
                },
            )
        }
    }

}

