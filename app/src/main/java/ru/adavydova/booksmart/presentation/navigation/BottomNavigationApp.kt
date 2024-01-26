package ru.adavydova.booksmart.presentation.navigation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.adavydova.booksmart.R
import ru.adavydova.booksmart.presentation.main_screen.PermissionTextProvider

@Composable
fun BottomNavigationApp(
    checkingThePermission: (PermissionTextProvider, Boolean) -> Unit,
) {

    val navController = rememberNavController()
    val backStackState = navController.currentBackStackEntryAsState().value
    var selectedItem by rememberSaveable {
        mutableIntStateOf(0)
    }
    val bottomNavigationItems = listOf<BottomNavigationItem>(
        BottomNavigationItem(
            route = Route.HomeNavGraph.route,
            icon = Icons.Default.Home,
            name = "Home"
        ),
        BottomNavigationItem(
            route = Route.SearchNavGraph.route,
            icon = Icons.Default.Search,
            name = "Search book"
        ),
        BottomNavigationItem(
            route = Route.MusicNavGraph.route,
            icon = ImageVector.vectorResource(id = R.drawable.baseline_library_music_24),
            name = "Music"
        ),

        )

    selectedItem = remember(backStackState) {
        when (backStackState?.destination?.parent?.route) {
            Route.HomeNavGraph.route -> 0
            Route.SearchNavGraph.route -> 1
            Route.MusicNavGraph.route -> 2
            else -> 0
        }
    }


    val isBottomBarNotVisible = remember(backStackState) {
        backStackState?.destination?.route == Route.DetailBookScreen.route + "?bookId={bookId}" ||
                backStackState?.destination?.route == Route.ReadBookScreen.route + "?bookName={bookName}&bookUrl={bookUrl}"
    }



    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (!isBottomBarNotVisible) {
                AppBottomBar(
                    items = bottomNavigationItems,
                    select = selectedItem,
                    onItemClick = {
                        navigateToBottomBar(
                            navController = navController,
                            route = it
                        )
                    }
                )

            }
        }
    ) {

        Box(modifier = Modifier.padding(it)) {
            NavHost(
                navController = navController,
                checkingThePermission = checkingThePermission
            )
        }
    }


}

private fun navigateToBottomBar(navController: NavController, route: String) {
    navController.navigate(route) {
        Log.d("route", route)
        Log.d("graph", navController.currentDestination?.route?: "d")
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

data class BottomNavigationItem(
    val route: String,
    val icon: ImageVector,
    val name: String
)