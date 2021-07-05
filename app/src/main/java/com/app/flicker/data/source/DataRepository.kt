package com.app.flicker.data.source

import com.app.flicker.data.source.local.ILocalDataSource
import com.app.flicker.data.source.remote.IRemoteDataSource
import com.app.flicker.pojo.ResultState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DataRepository(public val localDataSource: ILocalDataSource,
                     public val remoteDataSource: IRemoteDataSource,
                     public val dispatcher: CoroutineDispatcher) : IDataRepository{
    override suspend fun getPhotoList(
        queryParams: Map<String, String>,
        page: Int
    ): Flow<ResultState> = flow {
        try {
            localDataSource.getPhotosAsPerPage(page).let {
                if (it.isNotEmpty()) {
                    emit(ResultState.Success(dataList = it) as ResultState)
                }
            }

            val data = remoteDataSource.getPhotos(queryParams)
            data.photos.photo?.forEach {
                it.pageNumber = page
            }
            if(page == 1) {
                localDataSource.deleteAll()
            }
            localDataSource.insertPhoto(data.photos.photo)

            emit(ResultState.Success(dataList = localDataSource.getPhotosAsPerPage(pageNumber = page)) as ResultState)

        } catch (ex : Exception) {
            emit(ResultState.Failure(ex) as ResultState)
        }

    }
}