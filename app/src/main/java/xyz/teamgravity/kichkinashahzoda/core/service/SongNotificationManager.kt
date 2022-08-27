package xyz.teamgravity.kichkinashahzoda.core.service

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import xyz.teamgravity.kichkinashahzoda.R

class SongNotificationManager(
    private val context: Context,
    private val token: MediaSessionCompat.Token,
    private val listener: PlayerNotificationManager.NotificationListener,
    private val onSongChange: () -> Unit,
) {

    companion object {
        const val CHANNEL_ID = "crybaby_music"
        const val NOTIFICATION_ID = 0x5454
    }

    private var manager: PlayerNotificationManager? = null

    init {
        initializeManager()
    }

    fun show(player: Player) {
        manager?.setPlayer(player)
    }

    fun hide() {
        manager?.setPlayer(null)
    }

    private fun initializeManager() {
        manager = PlayerNotificationManager.Builder(context, NOTIFICATION_ID, CHANNEL_ID)
            .setChannelNameResourceId(R.string.notification_channel_player)
            .setChannelDescriptionResourceId(R.string.notification_channel_description_player)
            .setMediaDescriptionAdapter(DescriptionAdapter(MediaControllerCompat(context, token)))
            .setNotificationListener(listener)
            .build().apply {
                setSmallIcon(R.drawable.ic_music)
                setMediaSessionToken(token)
            }
    }

    private inner class DescriptionAdapter(
        private val controller: MediaControllerCompat,
    ) : PlayerNotificationManager.MediaDescriptionAdapter {

        override fun getCurrentContentTitle(player: Player): CharSequence {
            onSongChange()
            return controller.metadata.description.title.toString()
        }

        override fun createCurrentContentIntent(player: Player): PendingIntent? {
            return controller.sessionActivity
        }

        override fun getCurrentContentText(player: Player): CharSequence {
            return controller.metadata.description.subtitle.toString()
        }

        override fun getCurrentLargeIcon(player: Player, callback: PlayerNotificationManager.BitmapCallback): Bitmap? {
            return BitmapFactory.decodeResource(context.resources, R.drawable.icon)
        }
    }
}