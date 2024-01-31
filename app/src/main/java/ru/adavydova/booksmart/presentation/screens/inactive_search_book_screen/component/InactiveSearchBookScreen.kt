package ru.adavydova.booksmart.presentation.screens.inactive_search_book_screen.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import ru.adavydova.booksmart.presentation.screens.inactive_search_book_screen.InactiveSearchBookScreenViewModel
import ru.adavydova.booksmart.presentation.screens.inactive_search_book_screen.InactiveSearchScreenEvent
import ru.adavydova.booksmart.presentation.screens.inactive_search_book_screen.ShowState
import ru.adavydova.booksmart.presentation.screens.inactive_search_book_screen.filters.FilterBooks
import ru.adavydova.booksmart.presentation.screens.inactive_search_book_screen.filters.LanguageRestrictFilterBooks
import ru.adavydova.booksmart.presentation.screens.inactive_search_book_screen.filters.OrderBooks
import ru.adavydova.booksmart.presentation.screens.inactive_search_book_screen.getShowState
import ru.adavydova.booksmart.presentation.screens.main_screen.PermissionTextProvider


@Composable
fun InactiveSearchBookScreen(
    modifier: Modifier = Modifier,
    navigateToDetailBook: (Book) -> Unit,
    navigateToOnActiveSearchScreen: (String) -> Unit,
    navigateToInactiveSearchScreen: (String) -> Unit,
    checkingThePermission: ((PermissionTextProvider, Boolean) -> Unit),
    viewModel: InactiveSearchBookScreenViewModel = hiltViewModel<InactiveSearchBookScreenViewModel>(),
) {

    val screenState by viewModel.screenState.collectAsState()
    val filterState by viewModel.stateFilterBook.collectAsState()
    val books = screenState.books?.collectAsLazyPagingItems()

    val errorState: MutableState<LoadState.Error?> = remember {
        mutableStateOf(null)
    }


    val toolbarHeightRange = with(LocalDensity.current) {
        MIN_TOOLBAR_HEIGHT.roundToPx()..MAX_TOOLBAR_HEIGHT.roundToPx()
    }
    val toolbarState = rememberSaveable(saver = MiExitUntilCollapsedState.Saver) {
        MiExitUntilCollapsedState(toolbarHeightRange)
    }
    val lazyState = rememberLazyListState()
    val der = remember { derivedStateOf { lazyState.firstVisibleItemScrollOffset } }

    LaunchedEffect(key1 = der.value, block = {

        if (lazyState.firstVisibleItemIndex < 1) {
            toolbarState.scrollValue = der.value
        }
    })

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            if (screenState.showSearchMenu == ShowState.Open) {

                SearchFilterWindow(
                    filters = viewModel.filters,
                    expended = {
                        when (it) {
                            FilterBooks::class -> screenState.showFilterMenu.invoke()
                            OrderBooks::class -> screenState.showOrderMenu()
                            LanguageRestrictFilterBooks::class -> screenState.showLanguageMenu()
                            else -> throw IllegalArgumentException("error type")
                        }
                    },
                    onExpended = { select , type->
                        viewModel.onEvent(InactiveSearchScreenEvent.OpenOrCloseFilterMenu(select.getShowState(), type))
                    },
                    closeMenu = {
                        viewModel.onEvent(InactiveSearchScreenEvent.CancelAndCloseSearchMenu)
                    },
                    insertFilter = {
                        viewModel.onEvent(InactiveSearchScreenEvent.InsertFilter)
                    },
                    onSelect = {
                        viewModel.onEvent(InactiveSearchScreenEvent.SelectFilterType(it))
                    },
                    select = {
                        when (it) {
                            FilterBooks::class -> filterState.filter.filter
                            OrderBooks::class -> filterState.orderType.order
                            LanguageRestrictFilterBooks::class -> filterState.languageRestrict.language
                            else -> throw IllegalArgumentException("error type")
                        }
                    },
                    name = {
                        when (it) {
                            FilterBooks::class -> "Filter type"
                            OrderBooks::class -> "Order by"
                            LanguageRestrictFilterBooks::class -> "Language"
                            else -> throw IllegalArgumentException("error type")
                        }
                    }

                )
            }
        },
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
                    query = screenState.query,
                    checkingThePermission = checkingThePermission,
                    openSearchFilterMenu = {
                        viewModel.onEvent(
                            InactiveSearchScreenEvent.OpenOrCloseSearchMenu(
                                it
                            )
                        )
                    }
                )

                books?.let { pagingBooks ->

                    ListBooksWithScrollState(
                        books = pagingBooks,
                        modifier = Modifier
                            .layoutId("books")
                            .padding(
                                top = 2.dp,
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 16.dp
                            ),
                        changeErrorState = { errorState.value = it },
                        navigateToDetail = navigateToDetailBook,
                        card = { book, isClicked ->
                            CardBookItem(book, isClicked)
                        },
                        lazyState = lazyState)
                }


            }


        }
    )
}
