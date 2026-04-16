package xyz.teamgravity.kichkinashahzoda.injection.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import xyz.teamgravity.coresdkandroid.notification.NotificationManager
import xyz.teamgravity.kichkinashahzoda.BuildConfig
import xyz.teamgravity.kichkinashahzoda.R
import xyz.teamgravity.kichkinashahzoda.core.service.SongNotificationManager
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var tree: Timber.Tree

    @Inject
    lateinit var notification: NotificationManager

    override fun onCreate() {
        super.onCreate()

        initializeTimber()
        createNotificationChannel()
    }

    private fun initializeTimber() {
        if (BuildConfig.DEBUG) Timber.plant(tree)
    }

    private fun createNotificationChannel() {
        notification.createChannel(
            id = SongNotificationManager.CHANNEL_ID,
            name = R.string.notification_channel_player,
            description = R.string.notification_channel_description_player,
            vibrate = false,
            showBadge = false,
            force = true
        )
    }
}