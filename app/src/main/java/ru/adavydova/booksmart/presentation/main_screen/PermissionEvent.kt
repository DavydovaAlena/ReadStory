package ru.adavydova.booksmart.presentation.main_screen

sealed class PermissionEvent {
    data class PermissionRequest(val permission: PermissionTextProvider, val isGranted: Boolean): PermissionEvent()
    object DismissPermissionDialog: PermissionEvent()
    object DismissPermissionDeclinedDialog: PermissionEvent()


}