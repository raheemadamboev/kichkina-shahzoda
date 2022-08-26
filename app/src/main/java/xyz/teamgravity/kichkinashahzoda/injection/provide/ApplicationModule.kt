package xyz.teamgravity.kichkinashahzoda.injection.provide

import android.app.Application
import com.google.android.exoplayer2.upstream.DefaultDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import timber.log.Timber
import xyz.teamgravity.kichkinashahzoda.core.service.SongServiceConnection
import xyz.teamgravity.kichkinashahzoda.data.repository.MainRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideDefaultDataSourceFactory(application: Application): DefaultDataSource.Factory = DefaultDataSource.Factory(application)

    @Provides
    @Singleton
    fun provideMainRepository(defaultDataSourceFactory: DefaultDataSource.Factory): MainRepository =
        MainRepository(defaultDataSourceFactory)

    @Provides
    @Singleton
    fun provideSongServiceConnection(application: Application): SongServiceConnection = SongServiceConnection(application)

    @Provides
    @Singleton
    fun provideTimberDebugTree(): Timber.DebugTree = Timber.DebugTree()
}