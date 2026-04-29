package xyz.teamgravity.kichkinashahzoda.presentation.screen.song.list

import android.app.Activity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
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
import xyz.teamgravity.coresdkandroid.update.UpdateManager
import xyz.teamgravity.kichkinashahzoda.core.extension.isPlaying
import xyz.teamgravity.kichkinashahzoda.core.service.SongServiceConnection
import xyz.teamgravity.kichkinashahzoda.data.mapper.toSong
import xyz.teamgravity.kichkinashahzoda.data.model.SongModel
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SongListViewModel @Inject constructor(
    private val connection: SongServiceConnection,
    private val review: ReviewManager,
    private val update: UpdateManager
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

    var updateAvailableType: UpdateManager.Type by mutableStateOf(UpdateManager.Type.None)
        private set

    var updateDownloadedShown: Boolean by mutableStateOf(false)
        private set

    private val _event = Channel<SongListEvent>()
    val event: Flow<SongListEvent> = _event.receiveAsFlow()

    init {
        subscribe()
        observe()
    }

    override fun onCleared() {
        super.onCleared()
        connection.unsubscribe()
    }

    private fun subscribe() {
        connection.subscribe()
    }

    private fun observe() {
        observeSongs()
        observeCurrentSong()
        observePlaybackState()
        observeReviewEvent()
        observeUpdateEvent()
    }

    private suspend fun handleReviewEvent(event: ReviewManager.ReviewEvent) {
        when (event) {
            ReviewManager.ReviewEvent.Eligible -> {
                delay(1.seconds)
                reviewShown = true
            }
        }
    }

    private suspend fun handleUpdateEvent(event: UpdateManager.UpdateEvent) {
        when (event) {
            is UpdateManager.UpdateEvent.Available -> {
                updateAvailableType = event.type
            }

            UpdateManager.UpdateEvent.StartDownload -> {
                _event.send(SongListEvent.DownloadAppUpdate)
            }

            UpdateManager.UpdateEvent.Downloaded -> {
                updateDownloadedShown = true
            }
        }
    }

    private fun observeSongs() {
        viewModelScope.launch {
            connection.songs.collectLatest { songs ->
                this@SongListViewModel.songs = songs
            }
        }
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

    private fun observeUpdateEvent() {
        viewModelScope.launch {
            update.event.collect { event ->
                handleUpdateEvent(event)
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

    fun onUpdateCheck() {
        update.monitor()
    }

    fun onUpdateDownload(launcher: ActivityResultLauncher<IntentSenderRequest>) {
        update.downloadAppUpdate(launcher)
    }

    fun onUpdateAvailableDismiss() {
        updateAvailableType = UpdateManager.Type.None
    }

    fun onUpdateAvailableConfirm() {
        viewModelScope.launch {
            _event.send(SongListEvent.DownloadAppUpdate)
        }
    }

    fun onUpdateDownloadedDismiss() {
        updateDownloadedShown = false
    }

    fun onUpdateInstall() {
        update.installAppUpdate()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Misc
    ///////////////////////////////////////////////////////////////////////////

    enum class SongListEvent {
        Review,
        DownloadAppUpdate;
    }
}