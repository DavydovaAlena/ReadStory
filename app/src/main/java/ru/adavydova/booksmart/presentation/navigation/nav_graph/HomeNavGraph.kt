package ru.adavydova.booksmart.presentation.navigation.nav_graph

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import ru.adavydova.booksmart.data.mapper.toMediaItem
import ru.adavydova.booksmart.presentation.navigation.Route
import ru.adavydova.booksmart.presentation.player.PlayerViewModel
import ru.adavydova.booksmart.presentation.player.component.AudioList
import ru.adavydova.booksmart.presentation.player.component.CompatPlayerView
import ru.adavydova.booksmart.presentation.player.component.ExpendedPlayerView
import ru.adavydova.booksmart.presentation.player.rememberManagedMediaController
import ru.adavydova.booksmart.presentation.screens.main_screen.PermissionTextProvider
import ru.adavydova.booksmart.service.PlayerState
import ru.adavydova.booksmart.service.playMediaAt
import ru.adavydova.booksmart.service.state
import ru.adavydova.booksmart.service.updatePlaylist


fun NavGraphBuilder.homeNavGraph(navController: NavController) {

    navigation(
        startDestination = Route.PersonalBooksScreen.route,
        route = Route.HomeNavGraph.route
    ) {

        composable(route = Route.PersonalBooksScreen.route) {

            Column(Modifier.fillMaxSize()) {
                Text(text = "Personal book screen")
            }

        }

    }
}


