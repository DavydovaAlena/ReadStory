package ru.adavydova.booksmart.presentation.component.search_item.middle_variant

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.adavydova.booksmart.domain.model.google_book.GoogleBook
import ru.adavydova.booksmart.presentation.component.book_item.RecentlyOpenedBookImageWithShadow
import ru.adavydova.booksmart.presentation.component.book_item.customShadowForTheBook
import ru.adavydova.booksmart.presentation.component.progress.ProgressReadingBookBlock
import ru.adavydova.booksmart.presentation.component.search_item.short_variant.parseUrlImage
import ru.adavydova.booksmart.presentation.screens.bookshelf_screen.composable.getAuthors

@Composable
fun CardBookItem(
    modifier : Modifier = Modifier,
    googleBook: GoogleBook,
    onClick: (GoogleBook) -> Unit,

    ) {

    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clickable { onClick(googleBook) }
            .customShadowForTheBook(width = 160.dp, alpha = 0.3f)
            .background(MaterialTheme.colorScheme.background)
    ) {

        RecentlyOpenedBookImageWithShadow(
            modifier = Modifier
                .padding(4.dp)
                .clip(RoundedCornerShape(0.dp))
                .height(160.dp)
                .width(100.dp),
            cover = googleBook.imageLinks?.parseUrlImage()?: "",
            heightBook = 160.dp,
        )

        ProgressReadingBookBlock(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            maxLinesTitle = 2,
            title = googleBook.title,
            authors = googleBook.authors,
        )

    }


}



