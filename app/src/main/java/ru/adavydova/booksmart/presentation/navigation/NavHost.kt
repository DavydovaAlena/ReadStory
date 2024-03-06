package ru.adavydova.booksmart.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import ru.adavydova.booksmart.presentation.navigation.nav_graph.favoriteBooksNavGraph
import ru.adavydova.booksmart.presentation.navigation.nav_graph.homeNavGraph
import ru.adavydova.booksmart.presentation.navigation.nav_graph.bookshelfNavGraph
import ru.adavydova.booksmart.presentation.navigation.nav_graph.searchNavGraph
import ru.adavydova.booksmart.presentation.permission_logic.PermissionTextProvider


@Composable
fun NavHost(
    navController: NavHostController,
    checkingThePermission: (PermissionTextProvider, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {


    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Route.HomeNavGraph.route
    ) {
        searchNavGraph(navController = navController, checkingThePermission = checkingThePermission)
        homeNavGraph(navController = navController)
        bookshelfNavGraph(
            navController = navController,
            checkingThePermission = checkingThePermission
        )
        favoriteBooksNavGraph(
            navController = navController
        )
    }

}



