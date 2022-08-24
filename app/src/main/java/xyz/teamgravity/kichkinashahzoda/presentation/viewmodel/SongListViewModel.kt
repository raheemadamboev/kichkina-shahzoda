package xyz.teamgravity.kichkinashahzoda.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import xyz.teamgravity.kichkinashahzoda.data.model.SongModel
import xyz.teamgravity.kichkinashahzoda.data.repository.MainRepository
import javax.inject.Inject

@HiltViewModel
class SongListViewModel @Inject constructor(
    repository: MainRepository,
) : ViewModel() {

    val songs: List<SongModel> by mutableStateOf(repository.getSongs())

    var currentSong: SongModel  by mutableStateOf(songs.first())
        private set

    var menuExpanded: Boolean by mutableStateOf(false)
        private set

    fun onMenuExpanded() {
        menuExpanded = true
    }

    fun onMenuCollapsed() {
        menuExpanded = false
    }
}