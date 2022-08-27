package xyz.teamgravity.kichkinashahzoda.data.repository

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import androidx.core.net.toUri
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import xyz.teamgravity.kichkinashahzoda.R
import xyz.teamgravity.kichkinashahzoda.core.constant.SongConst
import xyz.teamgravity.kichkinashahzoda.data.mapper.toMediaItem
import xyz.teamgravity.kichkinashahzoda.data.mapper.toMediaMetadataCompat
import xyz.teamgravity.kichkinashahzoda.data.model.AudioModel

class MainRepository(
    private val defaultDataSourceFactory: DefaultDataSource.Factory,
) {

    companion object {
        private val audios = listOf(
            AudioModel(id = 1, name = SongConst.NAME_1, audio = R.raw.audio_1),
            AudioModel(id = 2, name = SongConst.NAME_2, audio = R.raw.audio_2),
            AudioModel(id = 3, name = SongConst.NAME_3, audio = R.raw.audio_3)
        )
        private val mediaMetadatas = audios.map { it.toMediaMetadataCompat() }
        private val mediaItems = mediaMetadatas.map { it.toMediaItem() }
    }

    ///////////////////////////////////////////////////////////////////////////
    // GET
    ///////////////////////////////////////////////////////////////////////////

    fun getMediaMetadata(id: String): MediaMetadataCompat? {
        return mediaMetadatas.find { it.description.mediaId == id }
    }

    fun getMediaMetadatas(): List<MediaMetadataCompat> {
        return mediaMetadatas
    }

    fun getMediaMetadataIndex(metadata: MediaMetadataCompat): Int {
        return mediaMetadatas.indexOf(metadata)
    }

    fun getMediaItems(): List<MediaBrowserCompat.MediaItem> {
        return mediaItems
    }

    fun getMediaSources(): ConcatenatingMediaSource {
        val concatenatingMediaSource = ConcatenatingMediaSource()
        mediaMetadatas.forEach { metadata ->
            val mediaSource = ProgressiveMediaSource.Factory(defaultDataSourceFactory)
                .createMediaSource(MediaItem.fromUri(metadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI).toUri()))
            concatenatingMediaSource.addMediaSource(mediaSource)
        }
        return concatenatingMediaSource
    }
}