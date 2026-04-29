package xyz.teamgravity.kichkinashahzoda.presentation.screen.song.song

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
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
import xyz.teamgravity.kichkinashahzoda.data.model.AudioModel
import xyz.teamgravity.kichkinashahzoda.data.model.SongModel
import java.text.SimpleDateFormat
import javax.inject.Inject

@HiltViewModel
class SongViewModel @Inject constructor(
    private val connection: SongServiceConnection,
    private val formatter: SimpleDateFormat
) : ViewModel() {

    private companion object {
        const val DELAY = 250L
    }

    var song: SongModel? by mutableStateOf(null)
        private set

    var nextButtonEnabled: Boolean by mutableStateOf(false)
        private set

    var duration: Long by mutableLongStateOf(0L)
        private set

    var durationText: String by mutableStateOf(formatter.format(duration))
        private set

    var position: Long by mutableLongStateOf(0L)
        private set

    var positionText: String by mutableStateOf(formatter.format(position))
        private set

    var positionUser: Long by mutableLongStateOf(0L)
        private set

    var positionUserText: String by mutableStateOf(formatter.format(positionUser))
        private set

    var playing: Boolean by mutableStateOf(false)
        private set

    private var lastUserSeekCompletedTime: Long = 0 // used for skipping position update after user completed seeking to prevent lag animation

    init {
        observe()
    }

    private fun observe() {
        observeSong()
        observeDuration()
        observePosition()
        observeState()
    }

    private fun handlePosition(position: Long?) {
        if (position == null) return
        this@SongViewModel.position = position
        positionText = formatter.format(position)
    }

    private fun allowUpdatingPosition(): Boolean {
        return (System.currentTimeMillis() - lastUserSeekCompletedTime) > 200L
    }

    private fun observeSong() {
        viewModelScope.launch {
            connection.metadata.collectLatest { metadata ->
                song = metadata?.toSong()
                nextButtonEnabled = song?.id != AudioModel.Part3.id
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
                if (!allowUpdatingPosition()) continue
                handlePosition(position)
                delay(DELAY)
            }
        }
    }

    private fun observeState() {
        viewModelScope.launch {
            connection.state.collectLatest { playback ->
                playing = playback?.isPlaying ?: false
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // API
    ///////////////////////////////////////////////////////////////////////////

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
        lastUserSeekCompletedTime = System.currentTimeMillis()
        handlePosition(positionUser)
        connection.seek(positionUser)
    }
}