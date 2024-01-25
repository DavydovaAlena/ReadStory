package ru.adavydova.booksmart.presentation.start_search_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import ru.adavydova.booksmart.R
import ru.adavydova.booksmart.presentation.component.search_bar.InactiveSearchBar
import ru.adavydova.booksmart.presentation.main_screen.PermissionTextProvider


@Composable
fun StartSearchScreen(
    modifier: Modifier = Modifier,
    navigateToInactiveSearchScreen: (String)-> Unit,
    navigateToOnActiveSearchScreen: (String)-> Unit,
    checkingThePermission: (PermissionTextProvider, Boolean) -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 50.dp,
                bottom = 16.dp,
                start = 16.dp,
                end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            modifier = Modifier
                .width(200.dp)
                .layoutId("icon_program_image"),
            imageVector = if (isSystemInDarkTheme()) {
                ImageVector.vectorResource(id = R.drawable.color_logo___no_background)
            } else {
                ImageVector.vectorResource(id = R.drawable.black_logo___no_background)
            },
            contentDescription = null
        )


        InactiveSearchBar(
            modifier = Modifier
                .layoutId("search_bar")
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSecondary,
                    shape = CircleShape
                )
                .shadow(
                    elevation = 10.dp,
                    shape = CircleShape
                )
                .clip(CircleShape),
            useGoogleAssistant = navigateToInactiveSearchScreen,
            checkingThePermission = checkingThePermission,
            query = "",
            navigateToOnActiveSearchScreen = navigateToOnActiveSearchScreen
        )


    }
}