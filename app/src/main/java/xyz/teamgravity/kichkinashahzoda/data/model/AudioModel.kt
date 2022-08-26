package xyz.teamgravity.kichkinashahzoda.data.model

import androidx.annotation.RawRes

data class AudioModel(
    val id: Int,
    val name: String,
    @RawRes val audio: Int,
)
