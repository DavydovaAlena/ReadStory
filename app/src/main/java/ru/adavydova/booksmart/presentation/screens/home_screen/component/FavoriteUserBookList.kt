package ru.adavydova.booksmart.presentation.screens.home_screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.adavydova.booksmart.domain.model.Book
import ru.adavydova.booksmart.presentation.component.button.SeeAllButton
import ru.adavydova.booksmart.presentation.component.search_item.short_variant.parseUrlImage
import ru.adavydova.booksmart.presentation.screens.home_screen.FavoriteBooksViewModel


@Composable
fun FavoriteUserBookList(
    navigateToFavoriteScreen: ()->Unit,
    modifier: Modifier = Modifier,
    navigateToDetail: (Book)-> Unit,
    viewModel: FavoriteBooksViewModel = hiltViewModel()
) {

    val state = viewModel.favoriteBooksState.collectAsState()

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(350.dp)
    ) {

        Row(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {

            Text(
                modifier = Modifier,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                text = "Your favorite book"
            )

            SeeAllButton {
                navigateToFavoriteScreen()
            }
        }


        LazyRow(
            contentPadding = PaddingValues(start = 24.dp, end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
            
        ) {

            items(state.value.books) { book ->

                FavoriteUserBookListItem(
                    book = book,
                    modifier = Modifier.clickable {
                        navigateToDetail(book)
                    })
            }
        }

    }

}

@Composable
fun FavoriteUserBookListItem(
    modifier: Modifier = Modifier,
    book: Book,
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .padding(5.dp)
            .width(100.dp)
            .fillMaxHeight()
    ) {

        AsyncImage(
            contentScale = ContentScale.FillBounds,
            modifier = modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.secondary)
                .fillMaxWidth()
                .height(145.dp)
                .border(1.dp, MaterialTheme.colorScheme.surfaceVariant),
            model = ImageRequest.Builder(context)
                .data(book.imageLinks?.parseUrlImage())
                .build(),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(6.dp))

        Text(
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            text = book.title)

        Text(
            maxLines = 2,
            color = MaterialTheme.colorScheme.outline,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium,
            text = book.authors)
    }



}


//@Preview
//@Composable
//fun FavoriteUserBookListPreview() {
//
//    FavoriteUserBookList()
//}