package ru.adavydova.booksmart.presentation.screens.reader_screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PageSlider(
    modifier: Modifier = Modifier
) {
    var offsetX by remember {
        mutableFloatStateOf(0f)
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(30.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .height(1.dp)
                .background(MaterialTheme.colorScheme.onBackground)
        )

        Box(modifier = Modifier
            .width(2.dp)
            .height(4.dp)
            .align(Alignment.Center)) {

        }

    }
}


