package ru.adavydova.booksmart.presentation.screens.home_screen.component

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.adavydova.booksmart.presentation.component.book_item.RecentlyOpenedBookImageWithShadow
import ru.adavydova.booksmart.presentation.component.button.SeeAllOrAddNewBookBlock
import ru.adavydova.booksmart.presentation.component.progress.ProgressReadingBookBlock
import ru.adavydova.booksmart.presentation.screens.home_screen.RecentlyOpenedViewModel


@Composable
fun RecentlyOpenedBlock(
    modifier: Modifier = Modifier,
    navigateToBookshelfScreen: ()->Unit,
    navigateToReaderScreen: (Long)->Unit,
    viewModel: RecentlyOpenedViewModel = hiltViewModel()
) {

    val booksState by viewModel.recentlyOpenedBook.collectAsState()

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {


        SeeAllOrAddNewBookBlock(
            numberOfItems = booksState.books.size,
            addEvent = { navigateToBookshelfScreen()},
            seeAllEvent = { navigateToBookshelfScreen()},
            blockTitle = "RECENTLY OPENED"
        )

        Spacer(modifier = Modifier.height(16.dp))


        LazyRow(
            contentPadding = PaddingValues(start = 40.dp, end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()

        ) {

            items(booksState.books) {
                RecentlyOpenedBlockItem(
                    progress = it.progression,
                    cover = it.cover,
                    title = it.title,
                    navigateToReaderScreen = { navigateToReaderScreen(it.id) }
                )
            }
        }

    }

}

@Composable
fun RecentlyOpenedBlockItem(
    modifier: Modifier = Modifier,
    navigateToReaderScreen: ()->Unit,
    progress: String,
    cover: String,
    title: String,
) {
    Log.d("cover", cover)
    val context = LocalContext.current
    Column(
        modifier = modifier
            .padding(5.dp)
            .width(100.dp)
            .wrapContentHeight()
            .clickable { navigateToReaderScreen() }
    ) {


        RecentlyOpenedBookImageWithShadow(
            cover = cover)

        Spacer(modifier = Modifier.height(6.dp))

        ProgressReadingBookBlock(title = title, progress = progress.toFloat())

    }

}