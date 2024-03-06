package ru.adavydova.booksmart.presentation.navigation

sealed class Route(val route: String) {

    object SearchNavGraph: Route(route = "search_nav_host")

    //Search logic
    object StartSearchScreen: Route(route = "start_search_screen_route")
    object ActiveSearchScreen: Route(route= "active_search_screen_route")
    object InactiveSearchScreen: Route(route = "inactive_search_screen_route")
    object DetailBookScreen: Route(route = "detail_book_screen_route")
    object ReadBookScreen: Route(route = "read_book_screen_route")



    object FavoriteNavGraph: Route(route = "favorite_nav_graph")
    object FavoriteBooksScreen: Route(route = "favorite_book_screen_route")

    object HomeNavGraph: Route(route = "home_nav_graph")
    object PersonalBooksScreen: Route(route = "personal_books_screen_route")


    object BookshelfNavGraph : Route(route = "bookshelf_nav_graph")
    object BookshelfLibraryScreen : Route(route = "bookshelf_library_screen_route")
    object ReaderBookScreen : Route(route = "reader_book_screen_route")
}