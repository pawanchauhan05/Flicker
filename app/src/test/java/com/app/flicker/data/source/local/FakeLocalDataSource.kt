package com.app.flicker.data.source.local

import com.app.flicker.pojo.Photo


class FakeLocalDataSource(private var photoList: MutableList<Photo> = mutableListOf()) : ILocalDataSource {

    fun insertFakePhotos(photos: List<Photo>) {
        photoList.addAll(photos)
    }


    override fun insertPhoto(photo: Photo) {
        photoList.add(photo)
    }

    override fun insertPhoto(photos: MutableList<Photo>) {
        photoList.addAll(photos)
    }

    override fun getAllPhotos(): MutableList<Photo> {
        return photoList
    }

    override fun getPhotosAsPerPage(pageNumber: Int): MutableList<Photo> {
        return photoList.filter {
            it.pageNumber == pageNumber
        } as MutableList<Photo>
    }

    override fun deleteAll() {
        photoList.clear()
    }
}