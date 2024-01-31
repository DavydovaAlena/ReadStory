package ru.adavydova.booksmart.di

import android.app.Application
import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSession.ConnectionResult.AcceptedResultBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import dagger.hilt.components.SingletonComponent
import ru.adavydova.booksmart.data.local.repository.MusicRepositoryImpl
import ru.adavydova.booksmart.domain.repository.MusicRepository
import ru.adavydova.booksmart.domain.usecase.music.AudioUseCase
import ru.adavydova.booksmart.domain.usecase.music.GetAudioUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AudioModule {


    @Provides
    @Singleton
    fun provideMusicRepository(application: Application): MusicRepository{
        return MusicRepositoryImpl(application)
    }

    @Provides
    @Singleton
    fun provideAudioUseCase(musicRepository: MusicRepository): AudioUseCase{
        return AudioUseCase(
            getAudioUseCase = GetAudioUseCase(musicRepository)
        )
    }

}