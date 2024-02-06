package ru.adavydova.booksmart.presentation.screens.home_screen.component

import androidx.annotation.DrawableRes
import ru.adavydova.booksmart.R

data class Page(
    val title: String,
    val description: String,
    @DrawableRes val image: Int
)

val pages = listOf<Page>(
    Page(
        title = "Your book library",
        description = "A platform that lets you unlock the world of books with ease - wherever, whenever",
        image = R.drawable.pager_var_first
    ),
    Page(
        title = "Find your book",
        description = "Use the search function to find new stories and upload them",
        image = R.drawable.page_listen_audio_book
    )
)