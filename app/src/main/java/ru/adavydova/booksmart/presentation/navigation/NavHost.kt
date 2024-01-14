package ru.adavydova.booksmart.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import okhttp3.internal.threadName
import ru.adavydova.booksmart.presentation.permission_logic.PermissionTextProvider
import ru.adavydova.booksmart.presentation.inactive_search_book_screen.component.InactiveSearchBookScreen
import ru.adavydova.booksmart.presentation.search_book_enable_screen.component.SearchFullWindowScreen
import ru.adavydova.booksmart.presentation.start_search_screen.StartSearchScreen


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
        startDestination = Route.StartSearchScreen.route
    ) {


        composable(
            route = Route.StartSearchScreen.route
        ){
            StartSearchScreen(
                navigateToInactiveSearchScreen = {
                    navController.navigate(Route.InactiveSearchScreen.route + "?query=${it}") },
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
                    }
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
                    navController.navigate(Route.InactiveSearchScreen.route + "?query=${it}")
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

private fun navigateToInactiveScreen(navController:NavController, query:String){
    navController.currentBackStackEntry?.savedStateHandle?.set("query", query)
    navController.navigate(
        route = Route.InactiveSearchScreen.route
    )
}

fun NavOptionsBuilder.clearBackStack(navController: NavController) {
    popUpTo(navController.currentBackStackEntry?.destination?.route ?: return) {
        inclusive = true
    }
}