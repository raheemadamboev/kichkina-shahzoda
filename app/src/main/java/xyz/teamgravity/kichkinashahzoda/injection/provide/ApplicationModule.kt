package xyz.teamgravity.kichkinashahzoda.injection.provide

import android.app.Application
import com.google.android.exoplayer2.upstream.DefaultDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import timber.log.Timber
import xyz.teamgravity.coresdkandroid.crypto.CryptoManager
import xyz.teamgravity.coresdkandroid.notification.NotificationManager
import xyz.teamgravity.coresdkandroid.preferences.Preferences
import xyz.teamgravity.coresdkandroid.review.ReviewManager
import xyz.teamgravity.kichkinashahzoda.core.service.SongServiceConnection
import xyz.teamgravity.kichkinashahzoda.data.repository.MainRepository
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Singleton

private typealias AndroidNotificationManager = android.app.NotificationManager

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideDefaultDataSourceFactory(application: Application): DefaultDataSource.Factory = DefaultDataSource.Factory(application)

    @Provides
    @Singleton
    fun provideMainRepository(defaultDataSourceFactory: DefaultDataSource.Factory): MainRepository = MainRepository(defaultDataSourceFactory)

    @Provides
    @Singleton
    fun provideSongServiceConnection(application: Application): SongServiceConnection = SongServiceConnection(application)

    @Provides
    @Singleton
    fun provideTimberDebugTree(): Timber.DebugTree = Timber.DebugTree()

    @Provides
    @Singleton
    fun provideSimpleDateFormat(): SimpleDateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())

    @Provides
    @Singleton
    fun provideAndroidNotificationManager(application: Application): AndroidNotificationManager =
        application.getSystemService(AndroidNotificationManager::class.java)

    @Provides
    @Singleton
    fun provideNotificationManager(
        application: Application,
        androidNotificationManager: AndroidNotificationManager
    ): NotificationManager = NotificationManager(
        application = application,
        manager = androidNotificationManager
    )

    @Provides
    @Singleton
    fun provideCryptoManager(): CryptoManager = CryptoManager()

    @Provides
    @Singleton
    fun providePreferences(
        cryptoManager: CryptoManager,
        application: Application
    ): Preferences = Preferences(
        crypto = cryptoManager,
        context = application
    )

    @Provides
    @Singleton
    fun provideReviewManager(
        preferences: Preferences,
        application: Application
    ): ReviewManager = ReviewManager(
        preferences = preferences,
        context = application
    )
}