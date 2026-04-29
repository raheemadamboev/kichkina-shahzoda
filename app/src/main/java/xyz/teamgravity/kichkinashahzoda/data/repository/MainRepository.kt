package xyz.teamgravity.kichkinashahzoda.data.repository

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import androidx.core.net.toUri
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import xyz.teamgravity.kichkinashahzoda.data.mapper.toMediaItem
import xyz.teamgravity.kichkinashahzoda.data.mapper.toMediaMetadataCompat
import xyz.teamgravity.kichkinashahzoda.data.model.AudioModel

class MainRepository(
    private val defaultDataSourceFactory: DefaultDataSource.Factory
) {

    private companion object {
        val MEDIA_METADATAS: List<MediaMetadataCompat> = AudioModel.entries.map { it.toMediaMetadataCompat() }
        val MEDIA_ITEMS: List<MediaBrowserCompat.MediaItem> = MEDIA_METADATAS.map { it.toMediaItem() }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Get
    ///////////////////////////////////////////////////////////////////////////

    fun getMediaMetadata(id: String): MediaMetadataCompat? {
        return MEDIA_METADATAS.firstOrNull { it.description.mediaId == id }
    }

    fun getMediaMetadatas(): List<MediaMetadataCompat> {
        return MEDIA_METADATAS
    }

    fun getMediaMetadataIndex(metadata: MediaMetadataCompat): Int {
        return MEDIA_METADATAS.indexOf(metadata)
    }

    fun getMediaItems(): List<MediaBrowserCompat.MediaItem> {
        return MEDIA_ITEMS
    }

    fun getMediaSources(): ConcatenatingMediaSource {
        val concatenatingMediaSource = ConcatenatingMediaSource()
        MEDIA_METADATAS.forEach { metadata ->
            val mediaSource = ProgressiveMediaSource.Factory(defaultDataSourceFactory)
                .createMediaSource(MediaItem.fromUri(metadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI).toUri()))
            concatenatingMediaSource.addMediaSource(mediaSource)
        }
        return concatenatingMediaSource
    }
}