package xyz.teamgravity.kichkinashahzoda.core.service

import android.app.ForegroundServiceStartNotAllowedException
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.ServiceCompat
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import xyz.teamgravity.coresdkandroid.android.changeLocale
import xyz.teamgravity.kichkinashahzoda.core.constant.LanguageConst
import xyz.teamgravity.kichkinashahzoda.data.repository.MainRepository
import javax.inject.Inject

@AndroidEntryPoint
class SongService : MediaBrowserServiceCompat(), Player.Listener, PlayerNotificationManager.NotificationListener {

    companion object {
        const val ID = "xyz.teamgravity.kichkinashahzoda.ID"
        private const val TAG = "xyz.teamgravity.kichkinashahzoda.SongService"
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

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base?.changeLocale(LanguageConst.VALUE))
    }

    override fun onCreate() {
        super.onCreate()

        initializeMediaSession()
        initializeSessionToken()
        initializeMediaSessionConnector()
        initializePlayer()
        initializeSongNotificationManager()
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
        if (!playing && foregroundService) {
            ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_DETACH)
            foregroundService = false
        }
    }

    override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
        super.onNotificationCancelled(notificationId, dismissedByUser)
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
        foregroundService = false
        stopSelf()
    }

    override fun onNotificationPosted(notificationId: Int, notification: Notification, ongoing: Boolean) {
        super.onNotificationPosted(notificationId, notification, ongoing)

        val shouldBeForeground = ongoing || playing
        if (shouldBeForeground) {
            try {
                ServiceCompat.startForeground(
                    this,
                    SongNotificationManager.NOTIFICATION_ID,
                    notification,
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK else 0
                )
                foregroundService = true
            } catch (e: ForegroundServiceStartNotAllowedException) {
                Timber.e(e)
            }
        } else if (foregroundService) {
            ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_DETACH)
            foregroundService = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        exoplayer.removeListener(this)
        exoplayer.release()
    }

    private fun initializeMediaSession() {
        val intent = packageManager?.getLaunchIntentForPackage(packageName)?.let { intent ->
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        }

        session = MediaSessionCompat(this, TAG)
        session?.setSessionActivity(intent)
        session?.isActive = true
    }

    private fun initializeSessionToken() {
        sessionToken = requireNotNull(session).sessionToken
    }

    private fun initializeMediaSessionConnector() {
        val preparer = SongPlaybackPreparer(
            repository = repository,
            onPlaybackPrepare = { metadata ->
                preparePlayer(requireNotNull(metadata))
            }
        )

        val connector = MediaSessionConnector(requireNotNull(session))
        connector.setPlaybackPreparer(preparer)
        connector.setQueueNavigator(MusicQueueNavigator())
        connector.setPlayer(exoplayer)
    }

    private fun initializePlayer() {
        exoplayer.setMediaSource(repository.getMediaSources())
        exoplayer.addListener(this)
        exoplayer.prepare()
    }

    private fun initializeSongNotificationManager() {
        SongNotificationManager(
            context = this,
            token = requireNotNull(session).sessionToken,
            onSongChange = {
                connection.setDuration(if (exoplayer.duration == C.TIME_UNSET) 0L else exoplayer.duration)
            },
            listener = this
        ).show(exoplayer)
    }

    private fun preparePlayer(metadata: MediaMetadataCompat) {
        exoplayer.seekTo(repository.getMediaMetadataIndex(metadata), 0L)
        exoplayer.playWhenReady = true
    }

    ///////////////////////////////////////////////////////////////////////////
    // Misc
    ///////////////////////////////////////////////////////////////////////////

    private inner class MusicQueueNavigator : TimelineQueueNavigator(requireNotNull(session)) {
        override fun getMediaDescription(player: Player, windowIndex: Int): MediaDescriptionCompat {
            return repository.getMediaMetadatas()[windowIndex].description
        }
    }
}