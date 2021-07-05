package com.app.flicker.data.source.remote

import com.app.flicker.pojo.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface RemoteApi {

    @GET("services/rest")
    suspend fun getPhotoList(@QueryMap queryParams : Map<String, String>): Response

}