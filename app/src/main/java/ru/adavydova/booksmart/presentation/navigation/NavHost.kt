package ru.adavydova.booksmart.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import ru.adavydova.booksmart.presentation.navigation.nav_graph.homeNavGraph
import ru.adavydova.booksmart.presentation.navigation.nav_graph.musicNavGraph
import ru.adavydova.booksmart.presentation.permission_logic.PermissionTextProvider
import ru.adavydova.booksmart.presentation.navigation.nav_graph.searchNavGraph


@Composable
fun NavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    checkingThePermission: (PermissionTextProvider, Boolean) -> Unit,
) {


    NavHost(
        navController = navController,
        startDestination = Route.HomeNavGraph.route
    ) {
        searchNavGraph(navController = navController, checkingThePermission = checkingThePermission)
        homeNavGraph(navController = navController)
        musicNavGraph(navController = navController)
    }

}



