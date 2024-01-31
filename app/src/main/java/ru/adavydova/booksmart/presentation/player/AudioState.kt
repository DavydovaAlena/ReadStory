package ru.adavydova.booksmart.presentation.player

import ru.adavydova.booksmart.data.local.audio.AudioData

data class AudioState (
    val audioItems: List<AudioData> = emptyList(),
    val error: String? = null
)