package com.app.flicker.di

import com.app.flicker.data.source.local.FakeLocalDataSource
import com.app.flicker.data.source.local.ILocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [LocalDataSourceModule::class]
)
object FakeLocalDataSourceModule {

    @Singleton
    @Provides
    fun provideLocalDataSource(): ILocalDataSource {
        return FakeLocalDataSource()
    }
}
