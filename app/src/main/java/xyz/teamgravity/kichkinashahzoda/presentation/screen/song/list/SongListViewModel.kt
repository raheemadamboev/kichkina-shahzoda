package xyz.teamgravity.kichkinashahzoda.presentation.screen.song.list

import android.app.Activity
import android.support.v4.media.MediaBrowserCompat
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import xyz.teamgravity.coresdkandroid.review.ReviewManager
import xyz.teamgravity.kichkinashahzoda.core.extension.isPlaying
import xyz.teamgravity.kichkinashahzoda.core.service.SongService
import xyz.teamgravity.kichkinashahzoda.core.service.SongServiceConnection
import xyz.teamgravity.kichkinashahzoda.data.mapper.toSong
import xyz.teamgravity.kichkinashahzoda.data.model.SongModel
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SongListViewModel @Inject constructor(
    private val connection: SongServiceConnection,
    private val review: ReviewManager
) : ViewModel() {

    var songs: List<SongModel> by mutableStateOf(emptyList())
        private set

    var currentSong: SongModel? by mutableStateOf(null)
        private set

    var playing: Boolean by mutableStateOf(false)
        private set

    var menuExpanded: Boolean by mutableStateOf(false)
        private set

    var reviewShown: Boolean by mutableStateOf(false)
        private set

    private val _event = Channel<SongListEvent>()
    val event: Flow<SongListEvent> = _event.receiveAsFlow()

    private val listener: MediaBrowserCompat.SubscriptionCallback = object : MediaBrowserCompat.SubscriptionCallback() {
        override fun onChildrenLoaded(parentId: String, children: MutableList<MediaBrowserCompat.MediaItem>) {
            super.onChildrenLoaded(parentId, children)
            songs = children.map { it.toSong() }
        }
    }

    init {
        observe()
    }

    override fun onCleared() {
        super.onCleared()
        connection.unsubscribe(SongService.ID, listener)
    }

    private fun observe() {
        observeSongs()
        observeCurrentSong()
        observePlaybackState()
        observeReviewEvent()
    }

    private suspend fun handleReviewEvent(event: ReviewManager.ReviewEvent) {
        when (event) {
            ReviewManager.ReviewEvent.Eligible -> {
                delay(1.seconds)
                reviewShown = true
            }
        }
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

    private fun observeReviewEvent() {
        viewModelScope.launch {
            review.event.collect { event ->
                handleReviewEvent(event)
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // API
    ///////////////////////////////////////////////////////////////////////////

    fun onMenuExpand() {
        menuExpanded = true
    }

    fun onMenuCollapse() {
        menuExpanded = false
    }

    fun onPlayPause() {
        if (playing) connection.pause() else connection.play()
    }

    fun onSongHandle(song: SongModel) {
        connection.playSong(song.id)
    }

    fun onReviewDismiss() {
        reviewShown = false
    }

    fun onReviewDeny() {
        review.deny()
    }

    fun onReviewLater() {
        review.remindLater()
    }

    fun onReviewConfirm() {
        viewModelScope.launch {
            _event.send(SongListEvent.Review)
        }
    }

    fun onReview(activity: Activity?) {
        if (activity == null) {
            Timber.e("onReview(): activity is null! Aborted the operation.")
            return
        }

        review.review(activity)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Misc
    ///////////////////////////////////////////////////////////////////////////

    enum class SongListEvent {
        Review;
    }
}