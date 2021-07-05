package com.app.flicker.data.source.local

import com.app.flicker.pojo.Photo
import kotlinx.coroutines.CoroutineDispatcher


class LocalDataSource internal constructor(
    private val photoDao: PhotoDao,
    private val dispatcher: CoroutineDispatcher
) : ILocalDataSource {

    override fun insertPhoto(photo: Photo) {
        photoDao.insertPhoto(photo)
    }

    override fun insertPhoto(photos: MutableList<Photo>) {
        photoDao.insertPhoto(photos)
    }

    override fun getAllPhotos(): MutableList<Photo> {
        return photoDao.getAllPhotos()
    }

    override fun getPhotosAsPerPage(pageNumber: Int): MutableList<Photo> {
        return photoDao.getPhotosAsPerPage(pageNumber)
    }

    override fun deleteAll() {
        photoDao.deleteAll()
    }
}