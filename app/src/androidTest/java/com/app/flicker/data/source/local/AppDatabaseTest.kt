package com.app.flicker.data.source.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.flicker.pojo.Photo
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {

    private lateinit var photoDao: PhotoDao
    private lateinit var appDatabase: AppDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        photoDao = appDatabase.getPhotoDao()
    }

    @Test
    fun insertAndReadPhoto() = runBlocking {

        val photo = Photo(id = "51288698768", secret = "884e52cdd2",
            server = "0", farm = 0, title = "Quick analysis in ms excel  Work with analytics Quickly  MS Excel क धस टरक",
            pageNumber = 1)
        photoDao.insertPhoto(photo)

        val photoList = photoDao.getAllPhotos()
        Assert.assertTrue(photoList.contains(photo))
    }

    @Test
    fun insertAndReadPhotoList() = runBlocking {
        val photo1 = Photo(id = "51288698768", secret = "884e52cdd2",
            server = "0", farm = 0, title = "Quick analysis in ms excel  Work with analytics Quickly  MS Excel क धस टरक",
            pageNumber = 1)

        val photo2 = Photo(id = "51288711803", secret = "13e2de1efa",
            server = "65535", farm = 66, title = "New Fashion Women Smart Watch",
            pageNumber = 1)


        val listToInsert = mutableListOf<Photo>(photo1, photo2)
        photoDao.insertPhoto(listToInsert)
        val photoList = photoDao.getAllPhotos()
        Assert.assertEquals(photoList, listToInsert)
    }

    @Test
    fun insertAndReadPhotoAsPerPage() = runBlocking {
        val photo1 = Photo(id = "51288698768", secret = "884e52cdd2",
            server = "0", farm = 0, title = "Quick analysis in ms excel  Work with analytics Quickly  MS Excel क धस टरक",
            pageNumber = 1)

        val photo2 = Photo(id = "51288711803", secret = "13e2de1efa",
            server = "65535", farm = 66, title = "New Fashion Women Smart Watch",
            pageNumber = 2)

        val listToInsert = mutableListOf<Photo>(photo1, photo2)
        photoDao.insertPhoto(listToInsert)

        val pageNumber = 2

        val photoList = photoDao.getPhotosAsPerPage(pageNumber)
        Assert.assertTrue(photoList.size == 1)

    }

    @Test
    fun insertAndDeletePhoto() = runBlocking {
        val photo1 = Photo(id = "51288698768", secret = "884e52cdd2",
            server = "0", farm = 0, title = "Quick analysis in ms excel  Work with analytics Quickly  MS Excel क धस टरक",
            pageNumber = 1)

        photoDao.insertPhoto(photo1)
        photoDao.deleteAll()
        val photoList = photoDao.getAllPhotos()

        Assert.assertTrue(photoList.isEmpty())
    }

    @After
    fun closeDb() {
        appDatabase.close()
    }
}