package xyz.teamgravity.kichkinashahzoda.data.mapper

import android.support.v4.media.MediaBrowserCompat
import xyz.teamgravity.kichkinashahzoda.data.model.SongModel

fun MediaBrowserCompat.MediaItem.toSong(): SongModel {
    return SongModel(
        id = mediaId!!,
        name = description.title.toString()
    )
}