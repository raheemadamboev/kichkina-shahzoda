package xyz.teamgravity.kichkinashahzoda.data.model

import androidx.annotation.RawRes
import androidx.annotation.StringRes

data class SongModel(
    val id: Int,
    @StringRes val name: Int,
    @RawRes val song: Int,
)
