package ru.adavydova.booksmart.presentation.screens.home_screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.adavydova.booksmart.domain.model.google_book.GoogleBook
import ru.adavydova.booksmart.presentation.component.book_item.FavoriteBookWithShadow
import ru.adavydova.booksmart.presentation.component.book_item.RecentlyOpenedBookImage
import ru.adavydova.booksmart.presentation.component.button.SeeAllOrAddNewBookBlock
import ru.adavydova.booksmart.presentation.component.search_item.short_variant.parseUrlImage
import ru.adavydova.booksmart.presentation.screens.home_screen.FavoriteBooksViewModel


@Composable
fun FavoriteBookFromStoreBlock(
    navigateToFavoriteScreen: ()->Unit,
    navigateToOnActiveScreen: ()-> Unit,
    modifier: Modifier = Modifier,
    navigateToDetail: (GoogleBook)-> Unit,
    viewModel: FavoriteBooksViewModel = hiltViewModel()
) {

    val state = viewModel.favoriteBooksState.collectAsState()

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {

        SeeAllOrAddNewBookBlock(
            numberOfItems = state.value.googleBooks.size,
            addEvent = {navigateToOnActiveScreen() },
            seeAllEvent = {navigateToFavoriteScreen()},
            blockTitle = "FAVORITE BOOKS" )

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            contentPadding = PaddingValues(start = 40.dp, end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
            
        ) {

            items(state.value.googleBooks) { book ->

                FavoriteBookFromStoreBlockItem(
                    googleBook = book,
                    modifier = Modifier.clickable {
                        navigateToDetail(book)
                    })
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Divider(thickness = 2.dp, modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp))

    }

}

@Composable
fun FavoriteBookFromStoreBlockItem(
    modifier: Modifier = Modifier,
    googleBook: GoogleBook,
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .padding(5.dp)
            .width(100.dp)
            .fillMaxHeight()
    ) {

        FavoriteBookWithShadow(cover = googleBook.imageLinks?: "")

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            text = googleBook.title)

        Text(
            maxLines = 2,
            color = MaterialTheme.colorScheme.outline,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium,
            text = googleBook.authors)
    }



}


//@Preview
//@Composable
//fun FavoriteUserBookListPreview() {
//
//    FavoriteUserBookList()
//}