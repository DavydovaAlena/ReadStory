package ru.adavydova.booksmart

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import ru.adavydova.booksmart.presentation.permission_logic.MainViewModel
import ru.adavydova.booksmart.presentation.permission_logic.PermissionEvent
import ru.adavydova.booksmart.presentation.navigation.NavHost
import ru.adavydova.booksmart.presentation.search_book_screen.component.PermissionDeclinedDialog
import ru.adavydova.booksmart.presentation.search_book_screen.component.PermissionDialog
import ru.adavydova.booksmart.ui.theme.BookSmartTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            BookSmartTheme {

                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val viewModel = hiltViewModel<MainViewModel>()
                    val context = LocalContext.current
                    val permissionRequiredList = viewModel.permissionRequiredList

                    NavHost(
                        checkingThePermission = { permissionTextProvider, isGranted ->
                            viewModel.onPermissionEvent(
                                PermissionEvent.PermissionRequest(
                                    permission = permissionTextProvider,
                                    isGranted = isGranted
                                )
                            )
                        }
                    )

                    permissionRequiredList.forEach { permission ->

                        when (shouldShowRequestPermissionRationale(permission.permissionName)) {
                            true -> {
                                PermissionDialog(
                                    permissions = permission,
                                    dismissPermissionDialog = {
                                        viewModel.onPermissionEvent(
                                            PermissionEvent.DismissPermissionDialog
                                        )
                                    },
                                    onClickButtonOk = { isGranted, permissionTextProvider ->
                                        viewModel.onPermissionEvent(
                                            PermissionEvent.PermissionRequest(
                                                isGranted = isGranted,
                                                permission = permissionTextProvider
                                            )
                                        )
                                    })
                            }

                            false -> {
                                PermissionDeclinedDialog(
                                    permissions = permission.permissionName,
                                    onGoToAppSettingClick = {
                                        val intent =
                                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                                data = Uri.fromParts(
                                                    "package",
                                                    context.packageName,
                                                    null
                                                )
                                            }
                                        startActivity(intent)
                                    },
                                    dismissPermissionDialog = {
                                        viewModel.onPermissionEvent(PermissionEvent.DismissPermissionDeclinedDialog)
                                    })
                            }
                        }
                    }


                }
            }
        }
    }
}


