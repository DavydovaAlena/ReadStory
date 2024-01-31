package ru.adavydova.booksmart.domain.usecase.music

import ru.adavydova.booksmart.data.local.audio.AudioData
import ru.adavydova.booksmart.domain.repository.MusicRepository
import javax.inject.Inject

class GetAudioUseCase @Inject constructor(
    private val musicRepository: MusicRepository
) {
    suspend operator fun invoke(): List<AudioData>{
        return musicRepository.getAudioItems()
    }
}