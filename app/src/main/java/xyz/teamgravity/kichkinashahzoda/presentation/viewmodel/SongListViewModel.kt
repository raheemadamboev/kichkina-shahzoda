package xyz.teamgravity.kichkinashahzoda.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SongListViewModel @Inject constructor(

) : ViewModel() {

    var menuExpanded: Boolean by mutableStateOf(false)
        private set

    fun onMenuExpanded() {
        menuExpanded = true
    }

    fun onMenuCollapsed() {
        menuExpanded = false
    }
}