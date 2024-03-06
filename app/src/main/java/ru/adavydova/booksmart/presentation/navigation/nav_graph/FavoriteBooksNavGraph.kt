package ru.adavydova.booksmart.presentation.navigation.nav_graph

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.adavydova.booksmart.presentation.component.search_item.middle_variant.CardBookItem
import ru.adavydova.booksmart.presentation.component.search_item.middle_variant.ShimmerCardBookItem
import ru.adavydova.booksmart.presentation.navigation.Route
import ru.adavydova.booksmart.presentation.screens.home_screen.FavoriteBooksViewModel


fun NavGraphBuilder.favoriteBooksNavGraph(
    navController: NavController
) {
    navigation(
        route = Route.FavoriteNavGraph.route,
        startDestination = Route.FavoriteBooksScreen.route
    ) {


        composable(route = Route.FavoriteBooksScreen.route) {

            val viewModel = hiltViewModel<FavoriteBooksViewModel>()
            val state by viewModel.favoriteBooksState.collectAsState()


            Column(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {

                Text(
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    text = "Favorite books"
                )

                Spacer(modifier = Modifier.height(46.dp))

//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(1.dp)
//                        .background(MaterialTheme.colorScheme.onBackground)
//                )
                
                if (state.load) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        repeat(15) {
                            ShimmerCardBookItem()
                        }
                    }

                } else {

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {

                        items(state.googleBooks) { book ->

                            CardBookItem(
                                googleBook = book,
                                onClick = { navController.navigate(Route.DetailBookScreen.route + "?bookId=${it.id}") })
                            
                            Spacer(modifier = Modifier.height(8.dp))

                        }

                    }

                }
            }


        }
    }

}