package ru.adavydova.booksmart.presentation.search_book_enable_screen.component

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.adavydova.booksmart.presentation.permission_logic.PermissionTextProvider


@Composable
fun PermissionDialog(
    permissions: PermissionTextProvider,
    dismissPermissionDialog: () -> Unit,
    modifier: Modifier = Modifier,
    onClickButtonOk: (Boolean, PermissionTextProvider) -> Unit
) {

    val permissionContract = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        onClickButtonOk(isGranted, permissions)
    }

    AlertDialog(
        title = {
            Text(
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
                text = "Permission required"
            )
        },

        containerColor = Color.White,
        text = {
            Text(
                style = MaterialTheme.typography.bodySmall,
                text = permissions.permissionName
            )
        },
        onDismissRequest = dismissPermissionDialog,
        dismissButton = {
            Text(
                modifier = Modifier.clickable { dismissPermissionDialog() },
                text = "Deny"
            )
        },
        confirmButton = {
            Text(
                modifier = Modifier.clickable {
                    permissionContract.launch(permissions.permissionName)
                },
                text = "Allow"
            )
        })

}

@Composable
fun PermissionDeclinedDialog(
    permissions: String,
    onGoToAppSettingClick: () -> Unit,
    modifier: Modifier = Modifier,
    dismissPermissionDialog: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {


        Snackbar(
            action = {
                Text(
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable { onGoToAppSettingClick() },
                    text = "Setting"
                )
            },
            actionOnNewLine = false,
            dismissAction = { dismissPermissionDialog() },

            ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                style = MaterialTheme.typography.labelLarge,
                text = permissions
            )

        }

    }


}


