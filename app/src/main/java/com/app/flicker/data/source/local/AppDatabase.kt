package com.app.flicker.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.flicker.pojo.Photo

@Database(entities = [Photo::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getPhotoDao(): PhotoDao
}