package com.app.flicker.pojo

class Photos(
    val page: Int = 0,
    val pages: Int = 0,
    val perPage: Int = 5,
    val total: Int = 0,
    val photo: MutableList<Photo>
) {
}