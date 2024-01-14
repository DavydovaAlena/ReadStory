package ru.adavydova.booksmart.presentation.component.newsList

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun  ShimmerListOfSearchItems(
    modifier: Modifier = Modifier,
    simmerItem: @Composable ()-> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),

    ) {

        items(10){
            simmerItem()
        }

    }
}