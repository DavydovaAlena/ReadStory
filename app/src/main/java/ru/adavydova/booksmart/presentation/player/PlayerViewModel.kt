package ru.adavydova.booksmart.presentation.player

import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.adavydova.booksmart.domain.usecase.music.AudioUseCase
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val audioUseCase: AudioUseCase,
): ViewModel() {

    private val _isPlayerSetUp = MutableStateFlow(false)
    val isPlayerSetUp = _isPlayerSetUp.asStateFlow()

    private val audioUris = savedStateHandle.getStateFlow("audioUris", emptyList<MediaItem>())
    private val _audioState = MutableStateFlow<AudioState>(AudioState())
    val audioState = _audioState.asStateFlow()

    fun setupPlayer() {
        _isPlayerSetUp.update {
            true
        }
    }

    fun getAudioData(){
        viewModelScope.launch {
            val audioList = withContext(Dispatchers.IO){
                audioUseCase.getAudioUseCase()
            }
            val audioNewListId= audioList.map {
                Log.d("uri new", it.id.toString())
                it.id }
            val audioOldListId= audioState.value.audioItems.map {
                it.id }


            if (!audioNewListId.equalsIgnoreOrder(audioOldListId)){
                Log.d("d", audioOldListId.size.toString())

                _audioState.update { audio->
                    audio.copy(
                        audioItems = audio.audioItems + audioList
                    )
                }

            }

        }

    }

}

fun <T> List<T>.equalsIgnoreOrder(other: List<T>) = this.size == other.size && this.toSet() == other.toSet()