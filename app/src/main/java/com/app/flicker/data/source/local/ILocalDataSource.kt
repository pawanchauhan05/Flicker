package com.app.flicker.data.source.local

import com.app.flicker.pojo.Photo


interface ILocalDataSource {

    fun insertPhoto(photo : Photo)

    fun insertPhoto(photos: MutableList<Photo>)

    fun getAllPhotos() : MutableList<Photo>

    fun getPhotosAsPerPage(pageNumber : Int) : MutableList<Photo>

    fun deleteAll()

}