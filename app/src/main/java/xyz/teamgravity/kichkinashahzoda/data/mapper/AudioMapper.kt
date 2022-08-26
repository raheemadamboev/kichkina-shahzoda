package xyz.teamgravity.kichkinashahzoda.data.mapper

import android.support.v4.media.MediaMetadataCompat
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import xyz.teamgravity.kichkinashahzoda.core.constant.SongConst
import xyz.teamgravity.kichkinashahzoda.data.model.AudioModel

fun AudioModel.toMediaMetadataCompat(): MediaMetadataCompat {
    return MediaMetadataCompat.Builder()
        .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, SongConst.ARTIST)
        .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, id.toString())
        .putString(MediaMetadataCompat.METADATA_KEY_TITLE, name)
        .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, name)
        .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, RawResourceDataSource.buildRawResourceUri(audio).toString())
        .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, SongConst.ARTIST)
        .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION, SongConst.ARTIST)
        .build()
}