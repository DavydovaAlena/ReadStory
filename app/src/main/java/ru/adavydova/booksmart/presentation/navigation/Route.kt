package ru.adavydova.booksmart.presentation.navigation

sealed class Route(val route: String) {

    object SearchNavGraph: Route(route = "search_nav_host")

    object StartSearchScreen: Route(route = "start_search_screen_route")
    object ActiveSearchScreen: Route(route= "active_search_screen_route")
    object InactiveSearchScreen: Route(route = "inactive_search_screen_route")
}