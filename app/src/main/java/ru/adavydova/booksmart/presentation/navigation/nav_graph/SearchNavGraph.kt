package ru.adavydova.booksmart.presentation.navigation.nav_graph

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import ru.adavydova.booksmart.presentation.detail_book.DetailBookViewModel
import ru.adavydova.booksmart.presentation.detail_book.component.DetailBookScreen
import ru.adavydova.booksmart.presentation.detail_book.component.DetailTopBar
import ru.adavydova.booksmart.presentation.inactive_search_book_screen.component.InactiveSearchBookScreen
import ru.adavydova.booksmart.presentation.navigation.Route
import ru.adavydova.booksmart.presentation.permission_logic.PermissionTextProvider
import ru.adavydova.booksmart.presentation.search_book_enable_screen.component.SearchFullWindowScreen
import ru.adavydova.booksmart.presentation.start_search_screen.StartSearchScreen


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
                ){
                    type = NavType.StringType
                }
            )
        ){

            val viewModel = hiltViewModel<DetailBookViewModel>()
            val bookState by viewModel.bookState.collectAsState()
            Scaffold(
                topBar = {
                    DetailTopBar(
                        navigateBack = {
                            navController.popBackStack()
                        })
                }
            ) {padding->
                bookState.book?.let {
                    DetailBookScreen(
                        modifier = Modifier.padding(padding),
                        book = it )
                }

            }

        }

        composable(
            route = Route.StartSearchScreen.route
        ){
            StartSearchScreen(
                navigateToInactiveSearchScreen = {
                    navController.navigate(Route.InactiveSearchScreen.route + "?query=${it}")},
                navigateToOnActiveSearchScreen = {
                    navController.navigate(Route.ActiveSearchScreen.route+ "?query=${it}")
                },
                checkingThePermission = checkingThePermission,
            )
        }


        composable(
            route = Route.InactiveSearchScreen.route + "?query={query}",
            arguments = listOf(
                navArgument(
                    name = "query"
                ){
                    nullable = true
                    type = NavType.StringType
                }
            )
        ) {
            InactiveSearchBookScreen(
                checkingThePermission = checkingThePermission,
                navigateToInactiveSearchScreen = {
                    navController.navigate(Route.InactiveSearchScreen.route + "?query=${it}") },
                navigateToOnActiveSearchScreen = {
                    navController.navigate(Route.ActiveSearchScreen.route+ "?query=${it}")
                },
                navigateToDetailBook = {
                    Log.d("ID", it.id)
                    navController.navigate(Route.DetailBookScreen.route + "?bookId=${it.id}") }
            )
        }

        composable(
            route = Route.ActiveSearchScreen.route + "?query={query}",
            arguments = listOf(
                navArgument(
                    name = "query"
                ){
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
                    navController.navigate(Route.InactiveSearchScreen.route + "?query=${it}"){
                        navController.popBackStack()
                    }
                },
                navigateToDetail = {

                },
                useGoogleAssistant = {
                    navController.navigate(Route.InactiveSearchScreen.route + "?query=${it}")
                }
            )
        }

    }

}