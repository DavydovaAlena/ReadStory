package ru.adavydova.booksmart.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import ru.adavydova.booksmart.presentation.permission_logic.PermissionTextProvider
import ru.adavydova.booksmart.presentation.component.searchbar.SearchBookScreen
import ru.adavydova.booksmart.presentation.search_book_screen.component.SearchFullWindowScreen


@Composable
fun NavHost(
    checkingThePermission: (PermissionTextProvider, Boolean) -> Unit,
) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.SearchNavGraph.route
    ) {
        searchNavGraph(navController = navController, checkingThePermission = checkingThePermission)
    }

}


fun NavGraphBuilder.searchNavGraph(
    checkingThePermission: (PermissionTextProvider, Boolean) -> Unit,
    navController: NavController
) {

    navigation(
        route = Route.SearchNavGraph.route,
        startDestination = Route.SearchBookScreen.route
    ) {

        composable(
            route = Route.SearchBookScreen.route
        ) {

            SearchBookScreen(
                checkingThePermission = checkingThePermission,
                toSearchFullScreen = {
                    navController.navigate(Route.SearchBookFullWindowScreen.route)
                },
            )

        }

        composable(
            route = Route.SearchBookFullWindowScreen.route
        ) {
            SearchFullWindowScreen(
                backClick = {
                    navController.popBackStack(
                        route = Route.SearchBookFullWindowScreen.route,
                        saveState = false,
                        inclusive = true
                    )
                },
                checkingThePermission = checkingThePermission,
                goOnRequest = { },
                navigateToDetail = {}
            )
        }

    }

}

fun NavOptionsBuilder.clearBackStack(navController: NavController) {
    popUpTo(navController.currentBackStackEntry?.destination?.route ?: return) {
        inclusive = true
    }
}