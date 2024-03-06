package ru.adavydova.booksmart.presentation.permission_logic

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    val permissionRequiredList = mutableStateListOf<PermissionTextProvider>()

    fun onPermissionEvent(event: PermissionEvent) {
        when (event) {
            PermissionEvent.DismissPermissionDeclinedDialog -> {
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        delay(4000)
                    }
                    permissionRequiredList.removeLast()
                }
            }

            PermissionEvent.DismissPermissionDialog -> {
                permissionRequiredList.removeLast()
            }

            is PermissionEvent.PermissionRequest -> {
                if (!event.isGranted) {
                    permissionRequiredList.add(0, event.permission)
                }
            }
        }
    }
}