package ru.adavydova.booksmart.presentation.player.component

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.adavydova.booksmart.data.local.audio.AudioData
import ru.adavydova.booksmart.domain.usecase.music.AudioUseCase
import javax.inject.Inject

@HiltViewModel
class AudioFromExternalStorageViewModel
@Inject constructor(
    val audioUseCase: AudioUseCase
){

    init {

    }

}


data class AudioFromExternalStorageState(
    val load: Boolean = false,
    val audio: List<AudioData>
)