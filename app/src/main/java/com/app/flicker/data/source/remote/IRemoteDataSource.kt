package com.app.flicker.data.source.remote

import androidx.lifecycle.LiveData
import com.app.flicker.pojo.Response
import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface IRemoteDataSource {
    suspend fun getPhotos(queryParams : Map<String, String>): Response
}