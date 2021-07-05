package com.app.flicker.data.source.remote


import com.app.flicker.pojo.Response


class RemoteDataSource(private val remoteApi: RemoteApi) : IRemoteDataSource {

    override suspend fun getPhotos(queryParams: Map<String, String>): Response {
        return remoteApi.getPhotoList(queryParams)
    }
}