package xyz.teamgravity.kichkinashahzoda.core.service

import android.content.ComponentName
import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import xyz.teamgravity.kichkinashahzoda.data.mapper.toSong
import xyz.teamgravity.kichkinashahzoda.data.model.SongModel

class SongServiceConnection(
    private val context: Context,
    private val scope: CoroutineScope
) {

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    private val _songs = MutableStateFlow<List<SongModel>>(emptyList())
    val songs: StateFlow<List<SongModel>> = _songs.asStateFlow()

    private val _state = MutableStateFlow<PlaybackStateCompat?>(null)
    val state: StateFlow<PlaybackStateCompat?> = _state.asStateFlow()

    private val _metadata = MutableStateFlow<MediaMetadataCompat?>(null)
    val metadata: StateFlow<MediaMetadataCompat?> = _metadata.asStateFlow()

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration.asStateFlow()

    private val mediaBrowserCallback = MediaBrowserCallback()
    private val mediaSubscriptionCallback = MediaSubscriptionCallback()
    private val mediaControllerCallback = MediaControllerCallback()
    private val mediaBrowser: MediaBrowserCompat = MediaBrowserCompat(
        context,
        ComponentName(context, SongService::class.java),
        mediaBrowserCallback,
        null
    )

    private var mediaController: MediaControllerCompat? = null
    private var mediaControllerJob: Job? = null

    init {
        executeCommandIfAvailable {
            connectMediaBrowser()
        }
    }

    private fun executeCommandIfAvailable(command: suspend () -> Unit) {
        if (mediaControllerJob?.isActive == true) return
        mediaControllerJob = scope.launch {
            command()
        }
    }

    private suspend fun connectMediaBrowser() {
        mediaBrowser.connect()
        _isConnected.first { it }
    }

    private suspend fun getMediaController(): MediaControllerCompat {
        if (!mediaBrowser.isConnected || mediaController == null) {
            connectMediaBrowser()
        }

        return requireNotNull(mediaController)
    }

    ///////////////////////////////////////////////////////////////////////////
    // API
    ///////////////////////////////////////////////////////////////////////////

    fun disconnect() {
        _isConnected.tryEmit(false)
        mediaBrowser.disconnect()
        mediaController = null
    }

    fun subscribe() {
        scope.launch {
            isConnected.first { it }
            mediaBrowser.subscribe(SongService.ID, mediaSubscriptionCallback)
        }
    }

    fun unsubscribe() {
        mediaBrowser.unsubscribe(SongService.ID, mediaSubscriptionCallback)
    }

    fun play() {
        executeCommandIfAvailable {
            getMediaController().transportControls?.play()
        }
    }

    fun pause() {
        executeCommandIfAvailable {
            getMediaController().transportControls?.pause()
        }
    }

    fun playSong(id: String) {
        executeCommandIfAvailable {
            getMediaController().transportControls?.playFromMediaId(id, null)
        }
    }

    fun nextSong() {
        executeCommandIfAvailable {
            getMediaController().transportControls?.skipToNext()
        }
    }

    fun previousSong() {
        executeCommandIfAvailable {
            getMediaController().transportControls?.skipToPrevious()
        }
    }

    fun seek(position: Long) {
        executeCommandIfAvailable {
            getMediaController().transportControls?.seekTo(position)
        }
    }

    fun setDuration(duration: Long) {
        _duration.tryEmit(duration)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Misc
    ///////////////////////////////////////////////////////////////////////////

    private inner class MediaBrowserCallback : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            mediaController = MediaControllerCompat(context, mediaBrowser.sessionToken)
            mediaController?.registerCallback(mediaControllerCallback)
            _isConnected.tryEmit(true)
        }

        override fun onConnectionSuspended() {
            _isConnected.tryEmit(false)
        }

        override fun onConnectionFailed() {
            super.onConnectionFailed()
            _isConnected.tryEmit(false)
        }
    }

    private inner class MediaSubscriptionCallback : MediaBrowserCompat.SubscriptionCallback() {
        override fun onChildrenLoaded(parentId: String, children: MutableList<MediaBrowserCompat.MediaItem>) {
            super.onChildrenLoaded(parentId, children)
            _songs.tryEmit(children.map { it.toSong() })
        }
    }

    private inner class MediaControllerCallback : MediaControllerCompat.Callback() {
        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            _state.tryEmit(state)
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            _metadata.tryEmit(metadata)
        }

        override fun onSessionDestroyed() {
            disconnect()
        }
    }
}