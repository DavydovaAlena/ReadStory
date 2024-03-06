package ru.adavydova.booksmart.data.local.repository

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.adavydova.booksmart.data.local.audio.AudioData
import ru.adavydova.booksmart.domain.repository.MusicRepository
import javax.inject.Inject

class MusicRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : MusicRepository {
    private var mCursor: Cursor? = null
    private val projection = arrayOf(
        MediaStore.Audio.AudioColumns._ID,
        MediaStore.Audio.AudioColumns.TITLE,
        MediaStore.Audio.AudioColumns.DURATION,
        MediaStore.Audio.AudioColumns.ARTIST,
        MediaStore.Audio.AudioColumns.DISPLAY_NAME,
    )

    private var selectionClause: String? =
        "${MediaStore.Audio.AudioColumns.IS_MUSIC} = ? AND ${MediaStore.Audio.Media.MIME_TYPE} NOT IN (?, ?, ?)"
    private var selectionArg = arrayOf("1", "audio/amr", "audio/3gpp", "audio/aac")

    private val sortOrder = "${MediaStore.Audio.AudioColumns.DATA} ASC"
    override suspend fun getAudioItems(): List<AudioData> {
        val audioList = mutableListOf<AudioData>()
        mCursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selectionClause,
            selectionArg,
            sortOrder
        )
        mCursor?.use { cursor ->
            val idColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns._ID)
            val displayNameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DISPLAY_NAME)
            val artistColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST)
            val durationColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DURATION)
            val titleColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE)

            cursor.apply {

                while (moveToNext()) {
                    val displayName = getString(displayNameColumn)
                    val id = getLong(idColumn)
                    val artist = getString(artistColumn)
                    val duration = getInt(durationColumn)
                    val title = getString(titleColumn)
                    val uri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        id
                    )

                    val metadata = MediaMetadata.Builder()
                        .setDisplayTitle(displayName)
                        .setArtist(artist)
                        .setTitle(title)
                        .build()

                    val mediaItem = MediaItem.Builder()
                        .setUri(uri)
                        .setMediaId(id.toString())
                        .setMediaMetadata(metadata)
                        .build()

                    audioList.add(
                        AudioData(
                            uri =uri,
                            displayName = displayName,
                            id = id,
                            artist = artist,
                            title = title,
                            duration = duration,
                            mediaItem = mediaItem
                        )
                    )
                }
            }
        }
            return audioList
    }
}