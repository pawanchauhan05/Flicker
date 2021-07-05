package com.app.flicker.data.source

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.flicker.FakeResponseUtility
import com.app.flicker.data.source.local.FakeLocalDataSource
import com.app.flicker.data.source.local.ILocalDataSource
import com.app.flicker.data.source.remote.FakeRemoteDataSource
import com.app.flicker.data.source.remote.IRemoteDataSource
import com.app.flicker.pojo.Photo
import com.app.flicker.pojo.ResultState
import com.app.newsfeed.MainCoroutineRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.IsEqual
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@Config(application = HiltTestApplication::class)
class DataRepositoryTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Inject
    lateinit var fakeLocalDataSource: ILocalDataSource

    @Inject
    lateinit var fakeRemoteDataSource: IRemoteDataSource

    @Inject
    lateinit var coDispatcher: CoroutineDispatcher

    private lateinit var dataRepository: DataRepository

    @Before
    fun setUp() {
        // Populate @Inject fields in test class
        hiltRule.inject()
        dataRepository = DataRepository(fakeLocalDataSource, fakeRemoteDataSource, coDispatcher)
    }

    @Test
    fun getPhotosList_requestAllPhotosFromRemoteSource() = mainCoroutineRule.runBlockingTest {
        val response = dataRepository.remoteDataSource.getPhotos(emptyMap())
        Assert.assertThat(response.photos.photo, IsEqual(FakeResponseUtility.getResponseWith2Photo().photos.photo))
    }

    @Test
    fun getPhotosList_pageOne_successRemoteResponse() = mainCoroutineRule.runBlockingTest {

        val list = dataRepository.getPhotoList(emptyMap(), 1).toList()
        Assert.assertEquals(
            list, listOf(
                ResultState.Success(FakeResponseUtility.getResponseWith2Photo().photos.photo)
            )
        )
        Assert.assertEquals(
            dataRepository.localDataSource.getAllPhotos(),
            FakeResponseUtility.getResponseWith2Photo().photos.photo
        )
    }

    @Test
    fun getPhotosList_pageOne_errorRemoteResponse() = mainCoroutineRule.runBlockingTest {
        (fakeRemoteDataSource as FakeRemoteDataSource).setStatus(FakeRemoteDataSource.Data.SHOULD_RETURN_ERROR)

        val list = dataRepository.getPhotoList(emptyMap(), 1).toList()

        val expectedList = mutableListOf<ResultState>().apply {
            add(ResultState.Failure(FakeResponseUtility.getResponseWithError()))
        }

        Assert.assertEquals(list, expectedList)

    }

    @Test
    fun getPhotosList_pageOne_successLocalResponse_successRemoteResponse() = mainCoroutineRule.runBlockingTest {
        (fakeLocalDataSource as FakeLocalDataSource).insertFakePhotos(FakeResponseUtility.getResponseWith2Photo().photos.photo)
        (fakeLocalDataSource as FakeLocalDataSource).insertFakePhotos(FakeResponseUtility.getResponseForPage2().photos.photo)


        val list = dataRepository.getPhotoList(emptyMap(), 1).toList()

        val expectedList = mutableListOf<ResultState>().apply {
            add(ResultState.Success(FakeResponseUtility.getResponseWith2Photo().photos.photo))
            add(ResultState.Success(FakeResponseUtility.getResponseWith2Photo().photos.photo))
        }

        Assert.assertEquals(list, expectedList)

        Assert.assertEquals(dataRepository.localDataSource.getAllPhotos(), FakeResponseUtility.getResponseWith2Photo().photos.photo)
    }

    @Test
    fun getPhotosList_pageTwo_successLocalResponse_errorRemoteResponse() = mainCoroutineRule.runBlockingTest {
        (fakeLocalDataSource as FakeLocalDataSource).insertFakePhotos(FakeResponseUtility.getResponseWith2Photo().photos.photo)
        (fakeLocalDataSource as FakeLocalDataSource).insertFakePhotos(FakeResponseUtility.getResponseForPage2().photos.photo)
        (fakeRemoteDataSource as FakeRemoteDataSource).setStatus(FakeRemoteDataSource.Data.SHOULD_RETURN_ERROR)

        val list = dataRepository.getPhotoList(emptyMap(), 2).toList()

        val expectedList = mutableListOf<ResultState>().apply {
            add(ResultState.Success(FakeResponseUtility.getResponseForPage2().photos.photo))
            add(ResultState.Failure(FakeResponseUtility.getResponseWithError()))
        }

        Assert.assertEquals(list, expectedList)

        val localSourceList = mutableListOf<Photo>().apply {
            addAll(FakeResponseUtility.getResponseWith2Photo().photos.photo)
            addAll(FakeResponseUtility.getResponseForPage2().photos.photo)
        }

        Assert.assertEquals(dataRepository.localDataSource.getAllPhotos(), localSourceList)
    }
}

