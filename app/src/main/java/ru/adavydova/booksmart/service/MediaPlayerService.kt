package ru.adavydova.booksmart.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media3.common.C
import androidx.media3.common.C.CONTENT_TYPE_MOVIE
import androidx.media3.common.C.USAGE_MEDIA
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import dagger.hilt.android.AndroidEntryPoint
import ru.adavydova.booksmart.MainActivity
import ru.adavydova.booksmart.R

@UnstableApi
@AndroidEntryPoint
class MediaPlayerService : MediaSessionService() {

    private var _mediaSession: MediaSession? = null
    private val mediaSession get() = _mediaSession!!

    companion object {
        private const val NOTIFICATION_ID = 1233242
        private const val CHANNEL_ID =
            "booksmart/service/media_player_service/session_notification_channel_id"
        private const val immutableFlag = PendingIntent.FLAG_IMMUTABLE
    }

    override fun onCreate() {
        super.onCreate()

        val audioAttribute = androidx.media3.common.AudioAttributes.Builder()
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .setUsage(USAGE_MEDIA)
            .build()


        val player = ExoPlayer.Builder(this)
            .setHandleAudioBecomingNoisy(true)
            .setAudioAttributes(audioAttribute, true)
            .build()

        _mediaSession = MediaSession.Builder(this, player)
            .also { builder ->
                getSingleTopActivity()?.let {
                    builder.setSessionActivity(it)
                }
            }.build()
        setListener(MediaSessionServiceListener())
    }


    private fun getBackStackedActivity(): PendingIntent? {
        return TaskStackBuilder.create(this).run {
            addNextIntent(Intent(this@MediaPlayerService, MainActivity::class.java))
            getPendingIntent(0, immutableFlag or PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }

    @UnstableApi
    private inner class MediaSessionServiceListener : Listener {
        override fun onForegroundServiceStartNotAllowedException() {
            if (
                Build.VERSION.SDK_INT >= 33 &&
                checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            val notificationManagerCompat = NotificationManagerCompat.from(this@MediaPlayerService)
            ensureNotificationChannel(notificationManagerCompat)
            val builder =
                NotificationCompat.Builder(this@MediaPlayerService, CHANNEL_ID)
                    .setSmallIcon(R.drawable.icon_orange)
                    .setContentTitle(getString(R.string.notification_content_title))
                    .setStyle(
                        NotificationCompat.BigTextStyle()
                            .bigText(getString(R.string.notification_content_text))
                    )
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)
                    .also { builder ->
                        getBackStackedActivity()?.let {
                            builder.setContentIntent(it)
                        }
                    }
            notificationManagerCompat.notify(NOTIFICATION_ID, builder.build())
        }
    }

    private fun ensureNotificationChannel(notificationManagerCompat: NotificationManagerCompat) {

        if (notificationManagerCompat.getNotificationChannel(CHANNEL_ID) != null) {
            return
        }
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Playback cannot be resumed",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManagerCompat.createNotificationChannel(channel)
    }

    private fun getSingleTopActivity(): PendingIntent? {
        return PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            immutableFlag or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        val player = mediaSession.player
        if (!player.playWhenReady || player.mediaItemCount == 0) {
            stopSelf()
        }
    }

    override fun onDestroy() {
        _mediaSession?.run {
            getBackStackedActivity()?.let { setSessionActivity(it) }
            player.release()
            release()
            _mediaSession = null
        }
        clearListener()
        Log.d("Service", "OnDestroy")
        super.onDestroy()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return _mediaSession
    }

}