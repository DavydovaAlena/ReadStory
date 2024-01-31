package ru.adavydova.booksmart.domain.repository

import ru.adavydova.booksmart.data.local.audio.AudioData

interface MusicRepository {
    suspend fun getAudioItems(): List<AudioData>
}