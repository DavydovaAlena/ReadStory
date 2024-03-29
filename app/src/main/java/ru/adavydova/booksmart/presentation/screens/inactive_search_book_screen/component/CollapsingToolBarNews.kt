package ru.adavydova.booksmart.presentation.screens.inactive_search_book_screen.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import ru.adavydova.booksmart.R
import ru.adavydova.booksmart.presentation.component.search_bar.InactiveSearchBar
import ru.adavydova.booksmart.presentation.screens.inactive_search_book_screen.ShowState
import ru.adavydova.booksmart.presentation.permission_logic.PermissionTextProvider


@OptIn(ExperimentalMotionApi::class)
@Composable
fun SearchBooksMotionHandler(
    modifier: Modifier = Modifier,
    query: String,
    currentHeight:Dp,
    navigateToOnActiveSearchScreen: (String)->Unit,
    navigateToInactiveSearchScreen: (String)->Unit,
    openSearchFilterMenu: (ShowState)-> Unit,
    checkingThePermission: (PermissionTextProvider, Boolean) -> Unit,
    progress: Float
) {


    val context = LocalContext.current
    val motionScene = remember {
        context.resources.openRawResource(R.raw.motion_scene)
            .readBytes().decodeToString()
    }

    MotionLayout(

        modifier = Modifier
            .fillMaxWidth()
            .height((currentHeight.div(2.3f)))
            .statusBarsPadding(),
        motionScene = MotionScene(motionScene),
        progress = progress
    ) {

        Image(
            modifier = Modifier
                .layoutId("icon_program_image"),
            imageVector = ImageVector.vectorResource(id = R.drawable.color_logo___no_background),
            contentDescription = null
        )

        InactiveSearchBar(
            modifier = Modifier
                .layoutId("search_bar")
                .border(
                    width = 1.dp,
                    color = Color.DarkGray,
                    shape = CircleShape
                )
                .shadow(
                    elevation = 10.dp,
                    shape = CircleShape
                )
                .clip(CircleShape),
            query = query,
            openSearchFilterMenu = openSearchFilterMenu,
            navigateToOnActiveSearchScreen = navigateToOnActiveSearchScreen,
            useGoogleAssistant = navigateToInactiveSearchScreen,
            checkingThePermission = checkingThePermission
        )

    }


}










