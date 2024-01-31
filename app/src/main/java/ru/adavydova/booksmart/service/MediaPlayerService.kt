package ru.adavydova.booksmart.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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
        private val immutableFlag = PendingIntent.FLAG_IMMUTABLE
    }

    override fun onCreate() {
        super.onCreate()
        val player = ExoPlayer.Builder(this).build()

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
                    .setSmallIcon(R.drawable.icon_dark)
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
        mediaSession.run {
            getBackStackedActivity()?.let { setSessionActivity(it) }
            player.release()
            release()
            _mediaSession = null
        }
        clearListener()
        super.onDestroy()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

}