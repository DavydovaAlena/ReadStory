package ru.adavydova.booksmart.presentation.screens.reader_screen.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import ru.adavydova.booksmart.R
import ru.adavydova.booksmart.reader.DummyReaderInitData
import ru.adavydova.booksmart.reader.ReaderInitData
import ru.adavydova.booksmart.reader.VisualReaderInitData
import timber.log.Timber


@Composable
fun TopBarReader(
    initData: ReaderInitData,
    modifier: Modifier = Modifier,
    backPressed: () -> Unit,
    onTtsClick: () -> Unit,
    onMenuClick: () -> Unit,
    onSearch: () -> Unit,
    saveBookmark: () -> Unit,
    onSettingClick: () -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = MaterialTheme.colorScheme.onBackground

) {

    when (initData) {
        is DummyReaderInitData -> {
            Timber.d("dummy")
        }

        is VisualReaderInitData.MediaReaderInitData -> {
            Timber.d("media")
        }

        is VisualReaderInitData.EpubReaderInitData -> {
            Timber.d("epub")
            TopBarEpubReader(
                modifier = modifier,
                backPressed = backPressed,
                onTtsClick = onTtsClick,
                onSearch = onSearch,
                saveBookmark = saveBookmark,
                onSettingClick = onSettingClick,
                onMenuClick = onMenuClick,
                containerColor = containerColor,
                contentColor = contentColor
            )

        }

        is VisualReaderInitData.ImageReaderInitData -> {
            Timber.d("image")
        }

//        is VisualReaderInitData.PdfReaderInitData -> {
//            Timber.d("pdf")
//        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarEpubReader(
    modifier: Modifier = Modifier,
    backPressed: () -> Unit,
    onSearch: () -> Unit,
    onTtsClick: () -> Unit,
    saveBookmark: () -> Unit,
    onSettingClick: () -> Unit,
    onMenuClick: () -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = MaterialTheme.colorScheme.onBackground
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor,
            actionIconContentColor = contentColor,
            navigationIconContentColor = contentColor
        ),
        title = {},
        navigationIcon = {
            IconButton(onClick = backPressed) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        },
        actions = {
            IconButton(onClick = onSearch) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            }
            IconButton(onClick = onTtsClick) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.outline_volume_down_24),
                    contentDescription = null
                )
            }
            IconButton(onClick = saveBookmark) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.outline_bookmark_border_24),
                    contentDescription = null
                )
            }

            IconButton(onClick = onSettingClick) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.outline_settings_24),
                    contentDescription = null
                )
            }

            IconButton(onClick = onMenuClick) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = null)
            }
        })
}
