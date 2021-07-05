package com.app.flicker.data.source.remote

import com.app.flicker.FakeResponseUtility
import com.app.flicker.pojo.Response


class FakeRemoteDataSource :  IRemoteDataSource {

    enum class Data {
        SHOULD_RETURN_ERROR,
        SHOULD_RETURN_2_PHOTO,
        SHOULD_RETURN_EMPTY_PHOTO
    }

    private var status = Data.SHOULD_RETURN_2_PHOTO

    fun setStatus(value: Data) {
        status = value
    }

    override suspend fun getPhotos(queryParams: Map<String, String>): Response {
        return when(status) {
            Data.SHOULD_RETURN_ERROR -> throw FakeResponseUtility.getResponseWithError()
            Data.SHOULD_RETURN_2_PHOTO -> FakeResponseUtility.getResponseWith2Photo()
            Data.SHOULD_RETURN_EMPTY_PHOTO -> FakeResponseUtility.getResponseWithEmptyPhoto()
        }
    }
}