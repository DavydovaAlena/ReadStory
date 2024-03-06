package ru.adavydova.booksmart.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.sharp.Home
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.chrisbanes.haze.HazeDefaults
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import ru.adavydova.booksmart.R
import ru.adavydova.booksmart.presentation.permission_logic.PermissionTextProvider

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
            selectedIcon = Icons.Sharp.Home,
            unselectedIcon = Icons.Outlined.Home,
            name = "Home"
        ),
        BottomNavigationItem(
            route = Route.SearchNavGraph.route,
            selectedIcon = Icons.Filled.Search,
            unselectedIcon = Icons.Outlined.Search,
            name = "Search"
        ),
//        BottomNavigationItem(
//            route = Route.FavoriteNavGraph.route,
//            selectedIcon = Icons.Sharp.Favorite,
//            unselectedIcon = Icons.Outlined.FavoriteBorder,
//            name = "Favorite"
//        ),
        BottomNavigationItem(
            route = Route.BookshelfNavGraph.route,
            selectedIcon = ImageVector.vectorResource(id = R.drawable.baseline_local_library_24),
            unselectedIcon = ImageVector.vectorResource(id = R.drawable.outline_local_library_24),
            name = "Bookshelf"
        ),

        )

    selectedItem = remember(backStackState) {
        when (backStackState?.destination?.parent?.route) {
            Route.HomeNavGraph.route -> 0
            Route.SearchNavGraph.route -> 1
//            Route.FavoriteNavGraph.route -> 2
            Route.BookshelfNavGraph.route -> 2
            else -> 0
        }
    }


    val isBottomBarNotVisible = remember(backStackState) {
        backStackState?.destination?.route == Route.DetailBookScreen.route + "?bookId={bookId}" ||
                backStackState?.destination?.route == Route.ReadBookScreen.route + "?bookName={bookName}&bookUrl={bookUrl}" ||
                backStackState?.destination?.route == Route.ReaderBookScreen.route + "?bookId={bookId}"||
                backStackState?.destination?.route == Route.ActiveSearchScreen.route
    }

    val isFloatButtonVisible = remember(backStackState) {
        backStackState?.destination?.route == Route.PersonalBooksScreen.route
    }

    val hazeState = remember {
        HazeState()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (!isBottomBarNotVisible) {
                Box(
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 64.dp)
                        .fillMaxWidth()
                        .height(64.dp)
                        .hazeChild(state = hazeState, shape = RoundedCornerShape(10.dp))
                        .border(
                            width = 1.dp,
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = .8f),
                                    Color.White.copy(alpha = .2f)
                                ),

                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                ) {

                    BottomBarTabs(
                        tabs = bottomNavigationItems,
                        selectedTab = selectedItem,
                        onTabSelected = {
                            navController.navigateToBottomBar(
                                route = it.route
                            )
                        })
                }
            }
        }
    ) { padding ->

        NavHost(
            modifier = Modifier
                .haze(
                    hazeState,
                    HazeStyle(
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        blurRadius = 30.dp,
                    )
                )
                .padding(horizontal = padding.calculateTopPadding())
                .fillMaxSize(),
            navController = navController,
            checkingThePermission = checkingThePermission,
        )

    }


}

fun NavController.navigateToBottomBar(route: String) {
    navigate(route) {
        popUpTo(this@navigateToBottomBar.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

data class BottomNavigationItem(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val name: String
)