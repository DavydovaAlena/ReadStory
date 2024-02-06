package ru.adavydova.booksmart.presentation.screens.player_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.adavydova.booksmart.data.local.audio.AudioData
import ru.adavydova.booksmart.domain.usecase.music.AudioUseCase
import ru.adavydova.booksmart.util.Resource
import javax.inject.Inject

@HiltViewModel
class AudioFromExternalStorageViewModel
@Inject constructor(
    private val audioUseCase: AudioUseCase
) : ViewModel() {

    private val _audioState = MutableStateFlow(AudioFromExternalStorageState())
    val audioState = _audioState.asStateFlow()

    init {
        downloadAudioData()
    }

    fun downloadAudioData(){
        _audioState.value = audioState.value.copy(load = true)
        viewModelScope.launch {
            when (val result = withContext(Dispatchers.IO) { audioUseCase.getAudioUseCase() }) {
                is Resource.Error -> {
                    _audioState.update { state ->
                        state.copy(
                            error = result.message
                        )
                    }
                }

                is Resource.Success -> {
                    result.data?.let { data ->
                        _audioState.update { state ->
                            state.copy(audio = state.audio.plus(data))
                        }
                    }
                }
            }

        }
        _audioState.value = audioState.value.copy(load = false)
    }

}



data class AudioFromExternalStorageState(
    val load: Boolean = false,
    val audio: List<AudioData> = emptyList(),
    val error: String? = null
)

