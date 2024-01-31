package ru.adavydova.booksmart.presentation.player

import android.net.Uri
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

    private val audioUris = savedStateHandle.getStateFlow("audioUris", emptyList<Uri>())
    private val _audioState = MutableStateFlow<AudioState>(AudioState())
    val audioState = _audioState.asStateFlow()

    fun setupPlayer() {
        _isPlayerSetUp.update {
            true
        }
    }

    fun getAudioData(){
        viewModelScope.launch {
            val audioList = withContext(Dispatchers.Default){
                audioUseCase.getAudioUseCase()
            }
            _audioState.value = audioState.value.copy(audio = audioList)

            audioList.forEach { audioData ->
                savedStateHandle["audioUris"] = audioUris.value + audioData.uri
            }

        }


    }

}