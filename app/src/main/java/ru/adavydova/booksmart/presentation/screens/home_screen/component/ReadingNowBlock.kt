package ru.adavydova.booksmart.presentation.screens.home_screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.adavydova.booksmart.presentation.component.book_item.BookCardReadingNowWithShadow
import ru.adavydova.booksmart.presentation.component.progress.ProgressReadingBookBlock
import ru.adavydova.booksmart.presentation.screens.home_screen.RecentlyOpenedViewModel

@Composable
fun ReadingNowBlock(
    modifier: Modifier = Modifier,
    viewModel: RecentlyOpenedViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val recentlyOpenedBooksState by viewModel.recentlyOpenedBook.collectAsState()
    val book = if (recentlyOpenedBooksState.books.isNotEmpty()) {
        recentlyOpenedBooksState.books.first()
    } else {
        null
    }

    val progress = book?.progression?.toFloat() ?: 0f

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
    ) {

        BookCardReadingNowWithShadow(
            modifier = Modifier,
            title = book?.title ?: "",
            cover = book?.cover ?: "",
        )
        Spacer(modifier = Modifier.height(8.dp))

        ProgressReadingBookBlock(
            title = book?.title ?: "",
            progress = progress
        )

    }

}