package ru.adavydova.booksmart.domain.usecase.music


import ru.adavydova.booksmart.data.local.audio.AudioData
import ru.adavydova.booksmart.domain.repository.MusicRepository
import ru.adavydova.booksmart.util.Resource
import javax.inject.Inject

class GetAudioUseCase @Inject constructor(
    private val musicRepository: MusicRepository
) {
    suspend operator fun invoke(): Resource<List<AudioData>> {
        return try {
            val audioItems = musicRepository.getAudioItems()
            Resource.Success(
                data = audioItems
            )
        } catch (e:Exception){
            Resource.Error(
                message = e.message?: "Not found"
            )
        }
    }
}