package ru.adavydova.booksmart.presentation.navigation.nav_graph

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.adavydova.booksmart.R
import ru.adavydova.booksmart.presentation.component.search_bar.SearchBarWithASeparateSort
import ru.adavydova.booksmart.presentation.navigation.Route
import ru.adavydova.booksmart.presentation.screens.home_screen.component.FavoriteUserBookList
import ru.adavydova.booksmart.presentation.screens.home_screen.component.PageIndicator
import ru.adavydova.booksmart.presentation.screens.home_screen.component.pages
import ru.adavydova.booksmart.presentation.screens.inactive_search_book_screen.component.MiExitUntilCollapsedState


@OptIn(ExperimentalFoundationApi::class, ExperimentalMotionApi::class)
fun NavGraphBuilder.homeNavGraph(navController: NavController) {


    navigation(
        startDestination = Route.PersonalBooksScreen.route,
        route = Route.HomeNavGraph.route
    ) {

        composable(route = Route.PersonalBooksScreen.route) {
            val pagerState = rememberPagerState(initialPage = 0) { pages.size }


            val context = LocalContext.current

            val toolbarHeightRange = with(LocalDensity.current) {
                120..176
            }
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
                        .padding(padding),
                    motionScene = MotionScene(motionScene),
                    progress = toolbarState.progress
                ) {


                    SearchBarWithASeparateSort()

                    Box(
                        modifier = Modifier
                            .background(Color.Black)
                            .layoutId("shadow_box")
                    )

                    HorizontalPager(
                        modifier = Modifier
                            .fillMaxWidth()
                            .layoutId("pager"),
                        state = pagerState
                    ) { index ->

                        Image(
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.FillBounds,
                            bitmap = ImageBitmap.imageResource(id = pages[index].image),
                            contentDescription = null
                        )
                    }

                    PageIndicator(
                        modifier = Modifier
                            .width(80.dp)
                            .layoutId("page_indicator"),
                        pageSize = pagerState.pageCount,
                        pagerState = pagerState,
                        color = Color.White,
                        selectedPage = pagerState.currentPage
                    )

                    Text(
                        modifier = Modifier
                            .layoutId("title"),
                        text = pages[pagerState.currentPage].title,
                        maxLines = 2,
                        color = Color.White,
                        fontWeight = FontWeight.W800,
                        fontSize = 22.sp,
                    )


                    Column(
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(
                                    topEnd = 20.dp,
                                    topStart = 20.dp
                                )
                            )
                            .background(MaterialTheme.colorScheme.background)
                            .layoutId("column")
                            .fillMaxWidth()
                            .verticalScroll(scrollState,)

                    ) {


                        FavoriteUserBookList(
                            modifier = Modifier.padding(top = 16.dp),
                            navigateToFavoriteScreen = { navController.navigate(Route.FavoriteBooksScreen.route) },
                            navigateToDetail = {
                                navController.navigate(Route.DetailBookScreen.route + "?bookId=${it.id}")
                            })

                        (1..3).forEach { 
                            Text(text = "")
                        }

                        FavoriteUserBookList(
                            modifier = Modifier.padding(top = 16.dp),
                            navigateToFavoriteScreen = { navController.navigate(Route.FavoriteBooksScreen.route) },
                            navigateToDetail = {
                                navController.navigate(Route.DetailBookScreen.route + "?bookId=${it.id}")
                            })


                    }

                }

            }
        }


    }

}



