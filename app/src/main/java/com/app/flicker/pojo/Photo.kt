package com.app.flicker.pojo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Photo(
    @PrimaryKey
    @ColumnInfo(name = "photo_id")
    val id : String,

    @ColumnInfo(name = "secret")
    val secret : String,

    @ColumnInfo(name = "server")
    val server : String,

    @ColumnInfo(name = "farm")
    val farm : Int,

    @ColumnInfo(name = "title")
    val title : String,

    @ColumnInfo(name = "page_number")
    var pageNumber : Int
)
