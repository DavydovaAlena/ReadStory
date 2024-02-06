package ru.adavydova.booksmart.presentation.screens.player_screen

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import ru.adavydova.booksmart.domain.usecase.music.AudioUseCase
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val audioUseCase: AudioUseCase,
) : ViewModel() {




    private val _playerStateInitial = MutableStateFlow<PlayerScreenStateType>(PlayerScreenStateType.PlayerCountItemIsNull)
    private val _permissionAudioState = MutableStateFlow<Boolean>(false)


    val playerState = combine(_playerStateInitial, _permissionAudioState){ playerStateInitial, permissionState ->
        MusicPlayerState(
            playerState = playerStateInitial,
            permissionState = permissionState
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MusicPlayerState())


    fun requestingPermissionToAccessMediaData(result: Boolean){
        when(result){
            true -> _permissionAudioState.value = true
            false -> _permissionAudioState.value = false
        }
    }


    fun setStatePlayer(countItemPlayer: Int, currentStatePlayer: Int){

        Log.d("dfw", countItemPlayer.toString())
        Log.d("dfw", currentStatePlayer.toString())

        if (countItemPlayer == 0){
            _playerStateInitial.value = PlayerScreenStateType.PlayerCountItemIsNull
            return
        }
        when(currentStatePlayer){
            3 ->  _playerStateInitial.value = PlayerScreenStateType.Play
            else -> _playerStateInitial.value = PlayerScreenStateType.Ready
        }
    }


}

data class MusicPlayerState (
    val playerState: PlayerScreenStateType = PlayerScreenStateType.PlayerCountItemIsNull,
    val permissionState: Boolean = false,
    val load: Boolean = false,
    val error: String? = null
)

sealed class PlayerScreenStateType{

    object PlayerCountItemIsNull: PlayerScreenStateType()
    object Ready: PlayerScreenStateType()
    object Play: PlayerScreenStateType()
}
fun <T> List<T>.equalsIgnoreOrder(other: List<T>) =
    this.size == other.size && this.toSet() == other.toSet()