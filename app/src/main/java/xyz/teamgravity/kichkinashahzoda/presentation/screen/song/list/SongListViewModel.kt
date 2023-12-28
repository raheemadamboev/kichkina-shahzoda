package xyz.teamgravity.kichkinashahzoda.presentation.screen.song.list

import android.support.v4.media.MediaBrowserCompat
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import xyz.teamgravity.kichkinashahzoda.core.extension.isPlaying
import xyz.teamgravity.kichkinashahzoda.core.service.SongService
import xyz.teamgravity.kichkinashahzoda.core.service.SongServiceConnection
import xyz.teamgravity.kichkinashahzoda.data.mapper.toSong
import xyz.teamgravity.kichkinashahzoda.data.model.SongModel
import javax.inject.Inject

@HiltViewModel
class SongListViewModel @Inject constructor(
    private val connection: SongServiceConnection,
) : ViewModel() {

    var songs: List<SongModel> by mutableStateOf(emptyList())
        private set

    var currentSong: SongModel? by mutableStateOf(null)
        private set

    var playing: Boolean by mutableStateOf(false)
        private set

    var menuExpanded: Boolean by mutableStateOf(false)
        private set

    private val listener = object : MediaBrowserCompat.SubscriptionCallback() {
        override fun onChildrenLoaded(parentId: String, children: MutableList<MediaBrowserCompat.MediaItem>) {
            super.onChildrenLoaded(parentId, children)
            songs = children.map { it.toSong() }
        }
    }

    init {
        observe()
    }

    fun onMenuExpand() {
        menuExpanded = true
    }

    fun onMenuCollapse() {
        menuExpanded = false
    }

    fun onPlayPause() {
        if (playing) connection.pause() else connection.play()
    }

    fun onSongClick(song: SongModel) {
        connection.playSong(song.id)
    }

    private fun observe() {
        observeSongs()
        observeCurrentSong()
        observePlaybackState()
    }

    private fun observeSongs() {
        connection.subscribe(SongService.ID, listener)
    }

    private fun observeCurrentSong() {
        viewModelScope.launch {
            connection.metadata.collectLatest { metadata ->
                currentSong = metadata?.toSong()
            }
        }
    }

    private fun observePlaybackState() {
        viewModelScope.launch {
            connection.state.collectLatest { playback ->
                playing = playback?.isPlaying ?: false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        connection.unsubscribe(SongService.ID, listener)
    }
}