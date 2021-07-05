package com.app.flicker.ui.listing

import android.util.Log
import androidx.lifecycle.*
import com.app.flicker.data.source.IDataRepository
import com.app.flicker.pojo.EmptyViewState
import com.app.flicker.pojo.ResultState
import com.app.flicker.pojo.ViewType
import com.app.flicker.utilities.Config
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class DataViewModel@Inject constructor(
    private val dataRepository: IDataRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _empty = MutableLiveData<EmptyViewState>()
    val empty: LiveData<EmptyViewState> = _empty

    private val _photoList = MutableLiveData<ResultState>()
    val photoList: LiveData<ResultState> = _photoList


    fun getPhotos(page : Int) {
        _dataLoading.postValue(true)


        var queryParams = mutableMapOf(
            "method" to Config.METHOD,
            "api_key" to Config.API_KEY,
            "format" to Config.FORMAT,
            "nojsoncallback" to Config.NO_JSON_CALLBACK,
            "per_page" to Config.PER_PAGE,
            "page" to "$page",
        )

        viewModelScope.launch(dispatcher) {

            dataRepository.getPhotoList(queryParams, page).collect {
                when (it) {
                    is ResultState.Success -> {
                        if(it.dataList.isNotEmpty()) {
                            _photoList.postValue(ResultState.Success(it.dataList))
                        } else {
                            _empty.postValue(EmptyViewState(ViewType.LAST_PAGE, ""))
                        }
                    }

                    is ResultState.Failure -> {
                        when (it.exception) {
                            is SocketTimeoutException -> {
                                _empty.postValue(EmptyViewState(ViewType.NETWORK_ERROR, "TIMEOUT ERROR!"))
                            }
                            is UnknownHostException -> {
                                _empty.postValue(EmptyViewState(ViewType.NETWORK_ERROR, "NO INTERNET!"))
                            }
                            else -> {
                                _empty.postValue(EmptyViewState(ViewType.NETWORK_ERROR,it.exception.message ?: "Something went wrong"))
                            }
                        }
                    }
                }
            }
        }.invokeOnCompletion {
            Log.e("TEMP", "getPhotos: " + it?.message)
            _dataLoading.postValue(false)
        }
    }
}

@Suppress("UNCHECKED_CAST")
class DataViewModelFactory @Inject constructor(
    private val dataRepository: IDataRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (DataViewModel(dataRepository, dispatcher) as T)
}