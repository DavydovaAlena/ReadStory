package ru.adavydova.booksmart.presentation.navigation.nav_graph

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import ru.adavydova.booksmart.presentation.navigation.Route
import ru.adavydova.booksmart.presentation.permission_logic.PermissionTextProvider
import ru.adavydova.booksmart.presentation.screens.bookshelf_screen.composable.BookshelfScreen
import ru.adavydova.booksmart.presentation.screens.reader_screen.component.ReadScreen

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.bookshelfNavGraph(
    navController: NavController,
    checkingThePermission: (PermissionTextProvider, Boolean) -> Unit
) {

    navigation(
        startDestination = Route.BookshelfLibraryScreen.route,
        route = Route.BookshelfNavGraph.route
    ) {

        composable(route = Route.BookshelfLibraryScreen.route) {

            BookshelfScreen(navController = navController)
//            MusicScreen(navController = navController,
//                checkingThePermission = checkingThePermission)
        }

        composable(
            route = Route.ReaderBookScreen.route + "?bookId={bookId}",
            arguments = listOf(
                navArgument(name = "bookId") {
                    type = NavType.LongType
                }
            )
        ) { argument ->

            ReadScreen(
                navController = navController
            )

        }

    }
}