package ru.adavydova.booksmart.presentation.player.component

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.launch
import ru.adavydova.booksmart.data.mapper.toMediaItem
import ru.adavydova.booksmart.presentation.navigation.Route
import ru.adavydova.booksmart.presentation.player.PlayerScreenStateType
import ru.adavydova.booksmart.presentation.player.PlayerViewModel
import ru.adavydova.booksmart.presentation.player.rememberManagedMediaController
import ru.adavydova.booksmart.presentation.screens.main_screen.PermissionTextProvider
import ru.adavydova.booksmart.service.PlayerState
import ru.adavydova.booksmart.service.playMediaAt
import ru.adavydova.booksmart.service.state
import ru.adavydova.booksmart.service.updatePlaylist

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.musicNavGraph(
    navController: NavController,
    checkingThePermission: (PermissionTextProvider, Boolean) -> Unit
) {

    navigation(
        startDestination = Route.PersonalMusicScreen.route,
        route = Route.MusicNavGraph.route
    ) {

        composable(route = Route.PersonalMusicScreen.route) {
            val viewModel = hiltViewModel<PlayerViewModel>()

            val playerScreenState by viewModel.playerState.collectAsStateWithLifecycle()

            val mediaController by rememberManagedMediaController()
            val coroutineScope = rememberCoroutineScope()

            var playerState: PlayerState? by remember {
                mutableStateOf(mediaController?.state())
            }


            val selectAudioLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(), onResult = { isGranted ->
                    checkingThePermission(PermissionTextProvider.ReadMediaTextProvider, isGranted)
                    viewModel.requestingPermissionToAccessMediaData(isGranted)
                })


            LaunchedEffect(key1 = Unit, block = {
                selectAudioLauncher.launch(PermissionTextProvider.ReadMediaTextProvider.permissionName)
            })


            LaunchedEffect(key1 = playerScreenState.playerState, block = {
                mediaController?.run {
                    Log.d("count", mediaItemCount.toString())
                    Log.d("count", playerScreenState.playerState.toString())

                    if (mediaItemCount > 0 && playerScreenState.permissionState && playerScreenState.playerState == PlayerScreenStateType.Ready) {
                        prepare()
                        play()
                        Log.d("d", "lan")
                        viewModel.setStatePlayer(
                            countItemPlayer = mediaItemCount,
                            currentStatePlayer = playerState?.mediaItemIndex ?: 0
                        )
                    }
                }
            })

            DisposableEffect(key1 = mediaController, effect = {
                mediaController?.run {
                    playerState = state()
                }
                onDispose {
                    playerState?.dispose()
                }
            })


            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            var openBottomSheet by remember { mutableStateOf(false) }

            if (openBottomSheet && playerState != null) {
                ModalBottomSheet(
                    onDismissRequest = { openBottomSheet = false },
                    shape = RectangleShape,
                    sheetState = sheetState
                ) {

                    ExpendedPlayerView(
                        playerState = playerState!!,
                        onCollapseTap = {
                            coroutineScope.launch {
                                sheetState.hide()
                                openBottomSheet = false
                            }
                        },
                        onPrevClick = {
                            mediaController?.seekToPreviousMediaItem()
                        },
                        onNextClick = {
                            mediaController?.seekToNextMediaItem()
                        })

                }
            }


            Box(
                modifier = Modifier
                    .fillMaxSize(),
            ) {

                AudioList(
                    onAudioClick = { index ->
                        viewModel.setStatePlayer(
                            countItemPlayer = mediaController?.mediaItemCount ?: 0,
                            currentStatePlayer = playerState?.playbackState ?: 0
                        )
                        mediaController?.playMediaAt(index = index)
                    },
                    mediaController = mediaController,
                    onUpdateList = { items ->
                        mediaController?.updatePlaylist(items.map { item -> item.toMediaItem() })
                    }
                )

                Spacer(modifier = Modifier.height(100.dp))

                if (playerState != null && playerState?.playbackState != 1) {
                    CompatPlayerView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                coroutineScope.launch {
                                    sheetState.expand()
                                    openBottomSheet = true
                                }
                            }
                            .align(Alignment.BottomCenter),
                        playerState = playerState!!,
                    )
                }

            }
        }


    }


}