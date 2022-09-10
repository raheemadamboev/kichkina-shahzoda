package xyz.teamgravity.kichkinashahzoda.data.mapper

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import androidx.core.net.toUri
import xyz.teamgravity.kichkinashahzoda.data.model.SongModel

fun MediaMetadataCompat.toMediaItem(): MediaBrowserCompat.MediaItem {
    val description = MediaDescriptionCompat.Builder()
        .setMediaUri(getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI).toUri())
        .setTitle(description.title)
        .setSubtitle(description.subtitle)
        .setMediaId(description.mediaId)
        .setIconUri(description.iconUri)
        .build()
    return MediaBrowserCompat.MediaItem(description, MediaBrowserCompat.MediaItem.FLAG_PLAYABLE)
}

fun MediaMetadataCompat.toSong(): SongModel {
    return SongModel(
        id = description.mediaId ?: "1", // it must not be null, but very few devices fuck it up, so first song is default song id
        name = description.title.toString()
    )
}