package com.app.flicker.pojo

sealed class ResultState {
    data class Success(val dataList : MutableList<Photo>) : ResultState()
    data class Failure(val exception : Exception) : ResultState()
    data class Progress(val isShow : Boolean) : ResultState()
}
