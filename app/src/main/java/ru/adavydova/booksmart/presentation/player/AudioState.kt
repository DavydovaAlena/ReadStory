package ru.adavydova.booksmart.presentation.player

import ru.adavydova.booksmart.data.local.audio.AudioData

data class AudioState (
    val audio: List<AudioData>? = null,
    val currentAudio: AudioData? = null,
    val error: String? = null
)