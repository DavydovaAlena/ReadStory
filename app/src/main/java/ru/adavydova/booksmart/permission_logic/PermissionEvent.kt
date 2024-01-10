package ru.adavydova.booksmart.permission_logic

sealed class PermissionEvent {
    data class PermissionRequest(val permission: PermissionTextProvider, val isGranted: Boolean): PermissionEvent()
    object DismissPermissionDialog: PermissionEvent()
    object DismissPermissionDeclinedDialog: PermissionEvent()


}