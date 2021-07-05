package com.app.flicker

import com.app.flicker.pojo.Photo
import com.app.flicker.pojo.Photos
import com.app.flicker.pojo.Response
import java.net.SocketTimeoutException

object FakeResponseUtility {

    private val ex : Exception = SocketTimeoutException("TIMEOUT ERROR!")
    private val emptyList = emptyList<Photo>()

    fun getResponseWith2Photo() : Response {
        val photo1 = Photo(id = "51288698768", secret = "884e52cdd2",
            server = "0", farm = 0, title = "Quick analysis in ms excel  Work with analytics Quickly  MS Excel क धस टरक",
            pageNumber = 1)

        val photo2 = Photo(id = "51288711803", secret = "13e2de1efa",
            server = "65535", farm = 66, title = "New Fashion Women Smart Watch",
            pageNumber = 1)

        return Response("ok", Photos(photo = mutableListOf(photo1, photo2)))
    }

    fun getResponseForPage2() : Response {
        val photo2 = Photo(id = "51287749027", secret = "68aeedd7c8",
            server = "65535", farm = 66, title = "quick analysis",
            pageNumber = 2)

        return Response("ok", Photos(photo = mutableListOf(photo2)))
    }

    fun getResponseWithError() : Exception {
        return ex
    }

    fun getResponseWithEmptyPhoto() : Response {
        return Response("ok", Photos(photo = mutableListOf()))
    }
}