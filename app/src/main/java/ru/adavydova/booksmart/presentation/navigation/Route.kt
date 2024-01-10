package ru.adavydova.booksmart.presentation.navigation

sealed class Route(val route: String) {

    object SearchNavGraph: Route(route = "search_nav_host")
    object SearchBookFullWindowScreen: Route(route= "search_screen_route")
    object SearchBookScreen: Route(route = "search_book_screen")
}