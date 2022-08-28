package xyz.teamgravity.kichkinashahzoda.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import xyz.teamgravity.kichkinashahzoda.core.extension.currentPlaybackPosition
import xyz.teamgravity.kichkinashahzoda.core.extension.isPlaying
import xyz.teamgravity.kichkinashahzoda.core.service.SongServiceConnection
import xyz.teamgravity.kichkinashahzoda.data.mapper.toSong
import xyz.teamgravity.kichkinashahzoda.data.model.SongModel
import java.text.SimpleDateFormat
import javax.inject.Inject

@HiltViewModel
class SongViewModel @Inject constructor(
    private val connection: SongServiceConnection,
    private val formatter: SimpleDateFormat,
) : ViewModel() {

    companion object {
        private const val DELAY = 100L
    }

    var song: SongModel? by mutableStateOf(null)
        private set

    var duration: Long by mutableStateOf(0L)
        private set

    var durationText: String by mutableStateOf(formatter.format(duration))
        private set

    var position: Long by mutableStateOf(0L)
        private set

    var positionText: String by mutableStateOf(formatter.format(position))
        private set

    var positionUser: Long by mutableStateOf(0L)
        private set

    var positionUserText: String by mutableStateOf(formatter.format(positionUser))
        private set

    var playing: Boolean by mutableStateOf(false)
        private set

    init {
        observe()
    }

    fun onPlayPause() {
        if (playing) connection.pause() else connection.play()
    }

    fun onNextSong() {
        connection.nextSong()
    }

    fun onPreviousSong() {
        connection.previousSong()
    }

    fun onPositionUserChange(value: Long) {
        positionUser = value
        positionUserText = formatter.format(value)
    }

    fun onSeek() {
        connection.seek(positionUser)
    }

    private fun observe() {
        observeSong()
        observeDuration()
        observePosition()
        observePlaybackState()
    }

    private fun observeSong() {
        viewModelScope.launch {
            connection.metadata.collectLatest { metadata ->
                song = metadata?.toSong()
            }
        }
    }

    private fun observeDuration() {
        viewModelScope.launch {
            connection.duration.collectLatest { duration ->
                this@SongViewModel.duration = duration
                durationText = formatter.format(duration)
            }
        }
    }

    private fun observePosition() {
        viewModelScope.launch {
            while (isActive) {
                val position = connection.state.value?.currentPlaybackPosition
                if (position != null) {
                    this@SongViewModel.position = position
                    positionText = formatter.format(position)
                }
                delay(DELAY)
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
}