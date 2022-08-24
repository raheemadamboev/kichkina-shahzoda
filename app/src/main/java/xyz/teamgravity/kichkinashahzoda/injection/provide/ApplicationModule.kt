package xyz.teamgravity.kichkinashahzoda.injection.provide

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import xyz.teamgravity.kichkinashahzoda.data.repository.MainRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideMainRepository(): MainRepository = MainRepository()
}