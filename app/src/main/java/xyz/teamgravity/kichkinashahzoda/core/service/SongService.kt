package xyz.teamgravity.kichkinashahzoda.core.service

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.content.ContextCompat
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import xyz.teamgravity.kichkinashahzoda.core.extension.stopForegroundSafely
import xyz.teamgravity.kichkinashahzoda.data.repository.MainRepository
import javax.inject.Inject

@AndroidEntryPoint
class SongService : MediaBrowserServiceCompat(), Player.Listener, PlayerNotificationManager.NotificationListener {

    companion object {
        private const val TAG = "SongService"
        const val ID = "xyz.teamgravity.kichkinashahzoda.ID"
    }

    @Inject
    lateinit var exoplayer: ExoPlayer

    @Inject
    lateinit var repository: MainRepository

    @Inject
    lateinit var connection: SongServiceConnection

    private var session: MediaSessionCompat? = null

    private var foregroundService = false
    private var playing = true

    override fun onCreate() {
        super.onCreate()

        initializeMediaSession()
        initializeSessionToken()
        initializeMediaSessionConnector()
        initializePlayer()
        initializeSongNotificationManager()
    }

    private fun initializeMediaSession() {
        val intent = packageManager?.getLaunchIntentForPackage(packageName)?.let { intent ->
            PendingIntent.getActivity(
                this,
                0,
                intent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
            )
        }

        session = MediaSessionCompat(this, TAG).apply {
            setSessionActivity(intent)
            isActive = true
        }
    }

    private fun initializeSessionToken() {
        sessionToken = session!!.sessionToken
    }

    private fun initializeMediaSessionConnector() {
        val preparer = SongPlaybackPreparer(
            repository = repository,
            onPlaybackPrepare = { metadata ->
                preparePlayer(metadata!!)
            }
        )

        MediaSessionConnector(session!!).apply {
            setPlaybackPreparer(preparer)
            setQueueNavigator(MusicQueueNavigator())
            setPlayer(exoplayer)
        }
    }

    private fun initializePlayer() {
        exoplayer.setMediaSource(repository.getMediaSources())
        exoplayer.addListener(this)
        exoplayer.prepare()
    }

    private fun initializeSongNotificationManager() {
        SongNotificationManager(
            context = this,
            token = session!!.sessionToken,
            listener = this,
            onSongChange = { connection.setDuration(if (exoplayer.duration == C.TIME_UNSET) 0L else exoplayer.duration) }
        ).show(exoplayer)
    }

    private fun preparePlayer(metadata: MediaMetadataCompat) {
        exoplayer.apply {
            seekTo(repository.getMediaMetadataIndex(metadata), 0L)
            playWhenReady = true
        }
    }

    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?): BrowserRoot {
        return BrowserRoot(ID, null)
    }

    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaBrowserCompat.MediaItem>>) {
        if (parentId == ID) result.sendResult(repository.getMediaItems().toMutableList())
    }

    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        super.onPlayWhenReadyChanged(playWhenReady, reason)
        playing = playWhenReady
        if (!playing) stopForegroundSafely(STOP_FOREGROUND_DETACH)
    }

    override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
        super.onNotificationCancelled(notificationId, dismissedByUser)
        stopForegroundSafely(STOP_FOREGROUND_REMOVE)
        foregroundService = false
        stopSelf()
    }

    override fun onNotificationPosted(notificationId: Int, notification: Notification, ongoing: Boolean) {
        super.onNotificationPosted(notificationId, notification, ongoing)
        when {
            ongoing && !foregroundService -> {
                ContextCompat.startForegroundService(this, Intent(applicationContext, this::class.java))
                startForeground(SongNotificationManager.NOTIFICATION_ID, notification)
                foregroundService = true
            }

            else -> {
                if (playing) {
                    startForeground(SongNotificationManager.NOTIFICATION_ID, notification)
                    foregroundService = true
                } else {
                    stopForegroundSafely(STOP_FOREGROUND_DETACH)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        exoplayer.removeListener(this)
        exoplayer.release()
    }

    private inner class MusicQueueNavigator : TimelineQueueNavigator(session!!) {
        override fun getMediaDescription(player: Player, windowIndex: Int): MediaDescriptionCompat {
            return repository.getMediaMetadatas()[windowIndex].description
        }
    }
}