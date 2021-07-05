package com.app.flicker.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.flicker.pojo.Photo

@Dao
interface PhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhoto(photo: Photo)

    @Query("SELECT * FROM photo")
    fun getAllPhotos(): MutableList<Photo>

    @Query("SELECT * FROM photo WHERE page_number = :pageNumber")
    fun getPhotosAsPerPage(pageNumber : Int): MutableList<Photo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhoto(photoList : MutableList<Photo>)

    @Query("DELETE FROM photo")
    fun deleteAll()
}