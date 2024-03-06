package ru.adavydova.booksmart.presentation.screens.bookshelf_screen.composable

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ru.adavydova.booksmart.presentation.component.book_item.RecentlyOpenedBookImageWithShadow
import ru.adavydova.booksmart.presentation.component.book_item.customShadowForTheBook
import ru.adavydova.booksmart.presentation.component.progress.ProgressReadingBookBlock
import ru.adavydova.booksmart.presentation.navigation.Route
import ru.adavydova.booksmart.presentation.screens.bookshelf_screen.BookshelfScreenEvent
import ru.adavydova.booksmart.presentation.screens.bookshelf_screen.BookshelfViewModel


fun String.getAuthors(): String = this.substringAfter("string=").substringBefore(")")

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BookshelfScreen(
    viewModel: BookshelfViewModel = hiltViewModel(),
    navController: NavController
) {

    val editModeState by viewModel.editMoreState.collectAsState()
    val statePublication by viewModel.statePublication.collectAsState()
    val showSnackbarState by viewModel.errorPublicationState.collectAsState()
    val bookshelfState by viewModel.bookshelfState.collectAsState()
    val appStoragePickedLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(), onResult = { uri ->
            uri?.let {
                viewModel.onEvent(BookshelfScreenEvent.ImportPublicationFromStorage(it))
            }
        })



    LaunchedEffect(key1 = editModeState.selectedItems) {
        if (editModeState.selectedItems.isEmpty()) {
            viewModel.onEvent(BookshelfScreenEvent.CloseEditingMode)
        }
    }

    LaunchedEffect(key1 = bookshelfState.success) {
        when (bookshelfState.success) {
            true -> {
                viewModel.onEvent(BookshelfScreenEvent.SuccessPublication)
            }

            false -> {
                BookshelfScreenEvent.ErrorPublication(
                    bookshelfState.error?.message ?: "eError publication"
                )
            }

            null -> {}
        }
    }


    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .fillMaxSize()
    ) {


        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .padding(
                    top = when (editModeState.editMode()) {
                        true -> 60.dp
                        false -> 0.dp
                    }
                )
                .align(Alignment.TopStart)
                .fillMaxWidth()
        ) {

            items(statePublication.books) { book ->

                val progression = when (book.progression) {
                    "{}" -> 0f
                    else -> book.progression?.substringAfter("\"totalProgression\":")
                        ?.substringBefore("}")?.toFloatOrNull()
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .customShadowForTheBook(width = 160.dp, alpha = 0.3f)
                        .background(MaterialTheme.colorScheme.background)
                        .combinedClickable(
                            onClick = {
                                if (editModeState.editMode()) {
                                    viewModel.onEvent(
                                        BookshelfScreenEvent.LongPressOnTheBook(
                                            book
                                        )
                                    )
                                } else {
                                    navController.navigate(Route.ReaderBookScreen.route + "?bookId=${book.id}")
                                }
                            },
                            onLongClick = {
                                viewModel.onEvent(
                                    BookshelfScreenEvent.LongPressOnTheBook(
                                        book
                                    )
                                )
                            }
                        )
                        .alpha(
                            if (editModeState.selectedItems.contains(book))
                                0.6f
                            else 1f
                        )) {

                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .clip(RoundedCornerShape(0.dp))
                            .height(160.dp)
                            .width(100.dp)
                    ) {

                        RecentlyOpenedBookImageWithShadow(
                            cover = book.cover, heightBook = 160.dp,
                        )

                        if (editModeState.editMode()) {
                            Checkbox(
                                modifier = Modifier.align(Alignment.TopStart),
                                checked = editModeState.selectedItems.contains(book),
                                onCheckedChange = {
                                    viewModel.onEvent(
                                        BookshelfScreenEvent.LongPressOnTheBook(
                                            book
                                        )
                                    )
                                })
                        }

                    }

                    ProgressReadingBookBlock(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        maxLinesTitle = 2,
                        title = book.title ?: "",
                        authors = book.author?.getAuthors(),
                        progress = progression ?: 0f
                    )


                }

            }

        }



        if (editModeState.editMode()) {

            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.onEvent(BookshelfScreenEvent.CloseEditingMode)
                    }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = null)
                    }
                },
                title = {
                    Text(text = "Selected items: ${editModeState.selectedItems.size}")
                },

                actions = {
                    IconButton(onClick = {
                        viewModel.onEvent(BookshelfScreenEvent.DeletePublication(editModeState.selectedItems))
                    }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                    }
                })

        }

        FloatingActionButton(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 80.dp)
                .align(Alignment.BottomCenter),
            onClick = {
                appStoragePickedLauncher.launch("*/*")
            }) {

            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
        }

        if (showSnackbarState.showSnackbar) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                text = showSnackbarState.message ?: ""
            )
        }

    }


}