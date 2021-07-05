package com.app.flicker.di

import com.app.flicker.data.source.DataRepository
import com.app.flicker.data.source.IDataRepository
import com.app.flicker.data.source.local.ILocalDataSource
import com.app.flicker.data.source.remote.IRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataRepositoryModule {

    @Singleton
    @Provides
    fun provideDataRepository(
        localDataSource: ILocalDataSource,
        remoteDataSource: IRemoteDataSource,
        dispatcher: CoroutineDispatcher
    ): IDataRepository {
        return DataRepository(
            localDataSource, remoteDataSource, dispatcher
        )
    }
}