package xyz.teamgravity.kichkinashahzoda.core.extension

import android.app.Service
import android.os.Build
import androidx.media.MediaBrowserServiceCompat

fun Service.stopForegroundSafely(behaviour: Int) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
        @Suppress("DEPRECATION")
        stopForeground(behaviour == MediaBrowserServiceCompat.STOP_FOREGROUND_REMOVE)
    } else {
        stopForeground(behaviour)
    }
}