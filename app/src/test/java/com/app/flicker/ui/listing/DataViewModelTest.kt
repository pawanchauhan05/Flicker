package com.app.flicker.ui.listing

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.flicker.FakeResponseUtility
import com.app.flicker.data.source.DataRepository
import com.app.flicker.data.source.local.ILocalDataSource
import com.app.flicker.data.source.remote.FakeRemoteDataSource
import com.app.flicker.data.source.remote.IRemoteDataSource
import com.app.flicker.pojo.EmptyViewState
import com.app.flicker.pojo.ResultState
import com.app.flicker.pojo.ViewType
import com.app.newsfeed.MainCoroutineRule
import com.app.newsfeed.getOrAwaitValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
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
@ExperimentalCoroutinesApi
class DataViewModelTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var fakeLocalDataSource: ILocalDataSource

    @Inject
    lateinit var fakeRemoteDataSource: IRemoteDataSource

    @Inject
    lateinit var dispatcher: CoroutineDispatcher

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var dataRepository: DataRepository
    private lateinit var dataViewModel: DataViewModel

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        hiltRule.inject()
        dataRepository = DataRepository(fakeLocalDataSource, fakeRemoteDataSource, dispatcher)
        dataViewModel = DataViewModel(dataRepository, dispatcher)

    }

    @Test
    fun getPhotos_successResponse_shouldDisplayList() = (dispatcher as TestCoroutineDispatcher).runBlockingTest {

        pauseDispatcher()
        dataViewModel.getPhotos(1)

        val data1 = dataViewModel.dataLoading.getOrAwaitValue()

        Assert.assertEquals(data1 , true)

        resumeDispatcher()

        val data2 = dataViewModel.photoList.getOrAwaitValue()
        Assert.assertEquals(data2 , ResultState.Success(FakeResponseUtility.getResponseWith2Photo().photos.photo))

        val data3 = dataViewModel.dataLoading.getOrAwaitValue()
        Assert.assertEquals(data3 , false)

    }

    @Test
    fun getPhotos_errorResponse_shouldDisplayErrorView() = (dispatcher as TestCoroutineDispatcher).runBlockingTest {
        (dataRepository.remoteDataSource as FakeRemoteDataSource).setStatus(FakeRemoteDataSource.Data.SHOULD_RETURN_ERROR)
        pauseDispatcher()

        dataViewModel.getPhotos(1)

        val data1 = dataViewModel.dataLoading.getOrAwaitValue()
        Assert.assertEquals(data1 , true)

        resumeDispatcher()

        val data2 = dataViewModel.empty.getOrAwaitValue()
        Assert.assertEquals(data2 , EmptyViewState(ViewType.NETWORK_ERROR, FakeResponseUtility.getResponseWithError().message!!))

        val data3 = dataViewModel.dataLoading.getOrAwaitValue()
        Assert.assertEquals(data3 , false)
    }

    @Test
    fun getPhotos_successResponse_shouldEmitLastPage() = (dispatcher as TestCoroutineDispatcher).runBlockingTest {
        (dataRepository.remoteDataSource as FakeRemoteDataSource).setStatus(FakeRemoteDataSource.Data.SHOULD_RETURN_EMPTY_PHOTO)
        pauseDispatcher()

        dataViewModel.getPhotos(1)

        val data1 = dataViewModel.dataLoading.getOrAwaitValue()
        Assert.assertEquals(data1 , true)

        resumeDispatcher()

        val data2 = dataViewModel.empty.getOrAwaitValue()
        Assert.assertEquals(data2 , EmptyViewState(ViewType.LAST_PAGE, ""))

        val data3 = dataViewModel.dataLoading.getOrAwaitValue()
        Assert.assertEquals(data3 , false)
    }



}
