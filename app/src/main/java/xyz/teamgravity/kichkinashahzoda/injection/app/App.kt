package xyz.teamgravity.kichkinashahzoda.injection.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import xyz.teamgravity.kichkinashahzoda.BuildConfig
import xyz.teamgravity.kichkinashahzoda.R
import xyz.teamgravity.kichkinashahzoda.core.service.SongNotificationManager
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var tree: Timber.DebugTree

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(tree)
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                SongNotificationManager.CHANNEL_ID,
                getString(R.string.notification_channel_player),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = getString(R.string.notification_channel_description_player)
                (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(this)
            }
        }
    }
}