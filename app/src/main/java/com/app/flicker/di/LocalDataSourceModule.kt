package com.app.flicker.di

import android.content.Context
import androidx.room.Room
import com.app.flicker.data.source.local.AppDatabase
import com.app.flicker.data.source.local.ILocalDataSource
import com.app.flicker.data.source.local.LocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object LocalDataSourceModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "flicker.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(appDatabase: AppDatabase, dispatcher: CoroutineDispatcher) : ILocalDataSource {
        return LocalDataSource(appDatabase.getPhotoDao(), dispatcher)
    }

}