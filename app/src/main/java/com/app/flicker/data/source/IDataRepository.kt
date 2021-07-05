package com.app.flicker.data.source

import com.app.flicker.pojo.ResultState
import kotlinx.coroutines.flow.Flow

interface IDataRepository {

    suspend fun getPhotoList(queryParams: Map<String, String>, page: Int) : Flow<ResultState>
}