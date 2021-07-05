package com.app.flicker.pojo

enum class ViewType{
    LAST_PAGE,
    NETWORK_ERROR
}

data class EmptyViewState(val viewType: ViewType, val message : String)
