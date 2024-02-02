package ru.adavydova.booksmart.presentation.navigation.nav_graph

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import ru.adavydova.booksmart.presentation.screens.detail_book_screen.DetailBookEvent
import ru.adavydova.booksmart.presentation.screens.detail_book_screen.DetailBookViewModel
import ru.adavydova.booksmart.presentation.screens.detail_book_screen.component.DetailBookScreen
import ru.adavydova.booksmart.presentation.screens.detail_book_screen.component.DetailTopBar
import ru.adavydova.booksmart.presentation.screens.detail_book_screen.component.WebViewBook
import ru.adavydova.booksmart.presentation.screens.inactive_search_book_screen.component.InactiveSearchBookScreen
import ru.adavydova.booksmart.presentation.screens.main_screen.PermissionTextProvider
import ru.adavydova.booksmart.presentation.navigation.Route
import ru.adavydova.booksmart.presentation.screens.search_book_enable_screen.component.SearchFullWindowScreen
import ru.adavydova.booksmart.presentation.screens.start_search_screen.StartSearchScreen


fun NavGraphBuilder.searchNavGraph(
    modifier: Modifier = Modifier,
    checkingThePermission: (PermissionTextProvider, Boolean) -> Unit,
    navController: NavController
) {


    navigation(
        route = Route.SearchNavGraph.route,
        startDestination = Route.StartSearchScreen.route
    ) {

        composable(
            route = Route.DetailBookScreen.route + "?bookId={bookId}",
            arguments = listOf(
                navArgument(
                    name = "bookId"
                ) {
                    type = NavType.StringType
                }
            )
        ) {

            val viewModel = hiltViewModel<DetailBookViewModel>()
            val bookState by viewModel.bookState.collectAsState()
            var toRead by remember {
                mutableStateOf<Boolean?>(null)
            }

            LaunchedEffect(key1 = toRead) {
                if (toRead == true) {
                    navController.navigate(
                        Route.ReadBookScreen.route +
                                "?bookName=${bookState.book?.title}&bookUrl=${bookState.book?.id}"
                    )
                }
            }

            Scaffold(
                topBar = {
                    DetailTopBar(
                        navigateBack = {
                            navController.popBackStack()
                        },
                        bookUrl = bookState.book?.id,
                        read = toRead,
                        bookName = bookState.book?.title,
                        toRead = { toRead = it },
                        favoriteState = bookState.favorite,
                        deleteOrInsertBook = { viewModel.onEvent(DetailBookEvent.DeleteOrInsertBook) })

                }
            ) { padding ->
                bookState.book?.let {
                    DetailBookScreen(
                        modifier = Modifier.padding(padding),
                        book = it
                    )
                }

            }

        }


        composable(
            route = Route.ReadBookScreen.route + "?bookName={bookName}&bookUrl={bookUrl}",
            arguments = listOf(
                navArgument("bookName") {
                    type = NavType.StringType
                },
                navArgument("bookUrl") {
                    type = NavType.StringType
                }
            )
        ) {

            val bookName = it.arguments?.getString("bookName")
            val bookUrl = it.arguments?.getString("bookUrl")

            if (bookName != null && bookUrl != null) {
                WebViewBook(
                    bookName = bookName,
                    url = bookUrl,
                    backPressed = { navController.popBackStack() })
            }
        }



        composable(
            route = Route.StartSearchScreen.route
        ) {
            StartSearchScreen(
                navigateToInactiveSearchScreen = {
                    navController.navigate(Route.InactiveSearchScreen.route + "?query=${it}")
                },
                navigateToOnActiveSearchScreen = {
                    navController.navigate(Route.ActiveSearchScreen.route + "?query=${it}")
                },
                checkingThePermission = checkingThePermission,
            )
        }


        composable(
            route = Route.InactiveSearchScreen.route + "?query={query}",
            arguments = listOf(
                navArgument(
                    name = "query"
                ) {
                    nullable = true
                    type = NavType.StringType
                }
            )
        ) {
            InactiveSearchBookScreen(
                checkingThePermission = checkingThePermission,
                navigateToInactiveSearchScreen = {
                    navController.navigate(Route.InactiveSearchScreen.route + "?query=${it}")
                },
                navigateToOnActiveSearchScreen = {
                    navController.navigate(Route.ActiveSearchScreen.route + "?query=${it}")
                },
                navigateToDetailBook = {
                    navController.navigate(Route.DetailBookScreen.route + "?bookId=${it.id}")
                }
            )
        }

        composable(
            route = Route.ActiveSearchScreen.route + "?query={query}",
            arguments = listOf(
                navArgument(
                    name = "query"
                ) {
                    nullable = true
                    type = NavType.StringType
                }
            )
        ) {
            SearchFullWindowScreen(
                backClick = {
                    navController.popBackStack(
                        route = Route.StartSearchScreen.route,
                        saveState = false,
                        inclusive = false
                    )
                },
                checkingThePermission = checkingThePermission,
                goOnRequest = {
                    navController.navigate(Route.InactiveSearchScreen.route + "?query=${it}") {
                        navController.popBackStack()
                    }
                },
                navigateToFullSearchScreen = {
                    navController.navigate(Route.InactiveSearchScreen.route + "?query=${it}") {
                        navController.popBackStack()
                    }
                },
                useGoogleAssistant = {
                    navController.navigate(Route.InactiveSearchScreen.route + "?query=${it}")
                }
            )
        }

    }
}

