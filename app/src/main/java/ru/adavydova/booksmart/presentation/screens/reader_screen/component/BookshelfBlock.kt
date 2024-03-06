package ru.adavydova.booksmart.presentation.screens.reader_screen.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import ru.adavydova.booksmart.presentation.component.book_item.customShadowForTheBook

@Composable
fun BookshelfBlock(
   modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth().height(15.dp)) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .height(5.dp)
            .zIndex(7f)
            .align(Alignment.TopCenter)
            .border(1.dp, Color.White)
            .customShadowForTheBook(width = 10.dp, alpha = 0.7f)) {
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(10.dp)
            .zIndex(4f)
            .align(Alignment.BottomCenter)
            .padding(horizontal = 5.dp)
            .background(Color.White)) {
        }

    }

}