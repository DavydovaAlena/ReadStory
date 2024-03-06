
package ru.adavydova.booksmart.presentation.component.book_item

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun BookCardWithCustomShadow(
    modifier: Modifier,
    bookCard:@Composable ()-> Unit,
    heightBookCard: Dp,
    alpha: Float = 0.1f
    ){

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(heightBookCard + 15.dp),
        ) {

        Box(
            modifier = Modifier
                .customShadowForTheBook(heightBookCard, alpha)
                .fillMaxWidth(0.7f)
                .height(heightBookCard)
                .align(Alignment.Center)
        )

        bookCard()

    }
}