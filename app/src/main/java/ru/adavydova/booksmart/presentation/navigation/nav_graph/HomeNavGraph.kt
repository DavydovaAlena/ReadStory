package ru.adavydova.booksmart.presentation.navigation.nav_graph

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.adavydova.booksmart.presentation.navigation.Route

fun NavGraphBuilder.homeNavGraph(navController: NavController) {

    navigation(
        startDestination = Route.PersonalBooksScreen.route,
        route = Route.HomeNavGraph.route
        ){

        composable(route = Route.PersonalBooksScreen.route){

            Column(Modifier.fillMaxSize()) {
                Text(text = "Personal book screen")
            }

        }

    }
}

fun NavGraphBuilder.musicNavGraph(navController: NavController) {

    navigation(
        startDestination = Route.PersonalMusicScreen.route,
        route = Route.MusicNavGraph.route
    ){

        composable(route = Route.PersonalMusicScreen.route){

            Column(Modifier.fillMaxSize()) {
                Text(text = "Personal music screen")
            }

        }

    }
}