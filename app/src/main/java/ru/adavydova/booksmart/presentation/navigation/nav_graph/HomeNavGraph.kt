package ru.adavydova.booksmart.presentation.navigation.nav_graph

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.adavydova.booksmart.R
import ru.adavydova.booksmart.presentation.component.search_bar.SearchBarWithASeparateSort
import ru.adavydova.booksmart.presentation.navigation.Route
import ru.adavydova.booksmart.presentation.navigation.navigateToBottomBar
import ru.adavydova.booksmart.presentation.screens.home_screen.component.FavoriteBookFromStoreBlock
import ru.adavydova.booksmart.presentation.screens.home_screen.component.HomeScreen
import ru.adavydova.booksmart.presentation.screens.home_screen.component.ReadingNowBlock
import ru.adavydova.booksmart.presentation.screens.home_screen.component.RecentlyOpenedBlock
import ru.adavydova.booksmart.presentation.screens.home_screen.component.pages
import ru.adavydova.booksmart.presentation.screens.inactive_search_book_screen.component.MiExitUntilCollapsedState


@OptIn(ExperimentalFoundationApi::class, ExperimentalMotionApi::class)
fun NavGraphBuilder.homeNavGraph(navController: NavController) {


    navigation(
        startDestination = Route.PersonalBooksScreen.route,
        route = Route.HomeNavGraph.route
    ) {

        composable(route = Route.PersonalBooksScreen.route) {
            HomeScreen(navController = navController)
        }

    }

}



