package ru.adavydova.booksmart.presentation.screens.home_screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.navigation.NavController
import ru.adavydova.booksmart.R
import ru.adavydova.booksmart.presentation.component.search_bar.SearchBarWithASeparateSort
import ru.adavydova.booksmart.presentation.navigation.Route
import ru.adavydova.booksmart.presentation.navigation.navigateToBottomBar
import ru.adavydova.booksmart.presentation.screens.inactive_search_book_screen.component.MiExitUntilCollapsedState

@OptIn(ExperimentalMotionApi::class)
@Composable
fun HomeScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val toolbarHeightRange = with(LocalDensity.current) { 40..300 }
    val toolbarState = rememberSaveable(saver = MiExitUntilCollapsedState.Saver) {
        MiExitUntilCollapsedState(toolbarHeightRange)
    }
    val scrollState = rememberScrollState()
    toolbarState.scrollValue = scrollState.value

    val motionScene = remember {
        context.resources.openRawResource(R.raw.detail_screen)
            .readBytes()
            .decodeToString()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()


    ) { padding ->


        MotionLayout(
            modifier = Modifier

                .fillMaxSize()
                .padding(bottom = padding.calculateBottomPadding()),
            motionScene = MotionScene(motionScene),
            progress = toolbarState.progress
        ) {


            SearchBarWithASeparateSort()


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .layoutId("pager"),
            ) {
            }

            Box(
                modifier = Modifier
                    .background(Color.LightGray)
                    .layoutId("pagerTint")
            )

            Text(
                modifier = Modifier
                    .layoutId("title"),
                text = "Reading Now",
                maxLines = 2,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.W700,
                fontSize = 20.sp,
            )


            Column(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topEnd = 20.dp,
                            topStart = 20.dp
                        )
                    )
                    .layoutId("column")
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(
                                Color.LightGray,
                                Color.White
                            )
                        )
                    )
                    .verticalScroll(scrollState)

            ) {



                ReadingNowBlock(modifier = Modifier.padding(horizontal = 30.dp))

                Spacer(modifier = Modifier.height(24.dp))

                RecentlyOpenedBlock(
                    navigateToBookshelfScreen = {
                        navController.navigateToBottomBar(Route.BookshelfLibraryScreen.route)
                    }, navigateToReaderScreen = {
                        navController.navigateToBottomBar(Route.ReaderBookScreen.route + "?bookId=$it")
                    })

                Spacer(modifier = Modifier.height(24.dp))


                FavoriteBookFromStoreBlock(
                    navigateToFavoriteScreen = { navController.navigate(Route.FavoriteBooksScreen.route) },
                    navigateToDetail = {
                        navController.navigateToBottomBar(Route.DetailBookScreen.route + "?bookId=${it.id}")
                    },
                    navigateToOnActiveScreen = {
                        navController.navigateToBottomBar(route = Route.ActiveSearchScreen.route)
                    })


                Spacer(modifier = Modifier.height(140.dp))

            }


        }

    }

}