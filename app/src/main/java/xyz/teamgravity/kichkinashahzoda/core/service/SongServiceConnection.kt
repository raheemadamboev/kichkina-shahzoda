package xyz.teamgravity.kichkinashahzoda.core.service

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class SongServiceConnection(
    private val context: Context,
) {

    private val _connected = Channel<Boolean>()
    val connected: Flow<Boolean> = _connected.receiveAsFlow()

    private val _state = MutableStateFlow<PlaybackStateCompat?>(null)
    val state: StateFlow<PlaybackStateCompat?> = _state.asStateFlow()

    private val _metadata = MutableStateFlow<MediaMetadataCompat?>(null)
    val metadata: StateFlow<MediaMetadataCompat?> = _metadata.asStateFlow()

    private val mediaBrowserCallback = MediaBrowserCallback()
    private val mediaControllerCallback = MediaControllerCallback()

    private val mediaBrowser = MediaBrowserCompat(
        context,
        ComponentName(context, SongService::class.java),
        mediaBrowserCallback,
        null
    ).apply { connect() }

    private var mediaController: MediaControllerCompat? = null

    fun subscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback) {
        mediaBrowser.subscribe(parentId, callback)
    }

    fun unsubscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback) {
        mediaBrowser.unsubscribe(parentId, callback)
    }

    fun play() {
        mediaController?.transportControls?.play()
    }

    fun pause() {
        mediaController?.transportControls?.pause()
    }

    fun playSong(id: String) {
        mediaController?.transportControls?.playFromMediaId(id, null)
    }

    private inner class MediaBrowserCallback : MediaBrowserCompat.ConnectionCallback() {

        override fun onConnected() {
            mediaController = MediaControllerCompat(context, mediaBrowser.sessionToken)
            mediaController?.registerCallback(mediaControllerCallback)
            _connected.trySend(true)
        }

        override fun onConnectionSuspended() {
            _connected.trySend(false)
        }

        override fun onConnectionFailed() {
            super.onConnectionFailed()
            _connected.trySend(false)
        }
    }

    private inner class MediaControllerCallback : MediaControllerCompat.Callback() {

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            _state.tryEmit(state)
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            _metadata.tryEmit(metadata)
        }

        override fun onSessionEvent(event: String?, extras: Bundle?) {
            super.onSessionEvent(event, extras)
        }

        override fun onSessionDestroyed() {
            mediaBrowserCallback.onConnectionSuspended()
        }
    }
}