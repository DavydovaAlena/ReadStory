package ru.adavydova.booksmart.presentation.component.newsList

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.adavydova.booksmart.presentation.component.search_item.short_variant.SimmerSearchItem

@Composable
fun  ShimmerListOfSearchItems(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),

    ) {

        items(10){
            SimmerSearchItem()
        }

    }
}