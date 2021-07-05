package com.app.flicker.ui.listing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.aghajari.zoomhelper.ZoomHelper
import com.app.flicker.R
import com.app.flicker.data.source.IDataRepository
import com.app.flicker.pojo.Photo
import com.app.flicker.pojo.ResultState
import com.app.flicker.pojo.ViewType
import com.app.flicker.utilities.PaginationScrollListener
import com.app.flicker.utilities.visibility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_data.*
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@AndroidEntryPoint
class DataActivity : AppCompatActivity() {

    @Inject
    lateinit var dataRepository: IDataRepository

    @Inject
    lateinit var dispatcher: CoroutineDispatcher

    private lateinit var dataListingAdapter: DataListingAdapter

    private val dataViewModel by viewModels<DataViewModel> {
        DataViewModelFactory(dataRepository, dispatcher)
    }

    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this)
    }

    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    var page: Int = 1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)
        dataListingAdapter = DataListingAdapter(mutableListOf())

        recyclerView.apply {
            layoutManager = linearLayoutManager
            itemAnimator = DefaultItemAnimator()
            adapter = dataListingAdapter
        }

        recyclerView.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
                page += 1
                dataViewModel.getPhotos(page)
            }

        })

        initObservers()

        dataViewModel.getPhotos(page)
    }

    private fun initObservers() {
        dataViewModel.photoList.observe(this, Observer {
            when (it) {
                is ResultState.Success -> {
                    isLoading = false
                    dataListingAdapter.updateList(it.dataList)

                    if(dataListingAdapter.list.isEmpty()) {
                        textViewError.text = "No Photos Found"
                        recyclerView.visibility = View.GONE
                        textViewError.visibility = View.VISIBLE
                    }
                }
                is ResultState.Failure -> {
                    isLoading = false
                }
            }
        })

        dataViewModel.dataLoading.observe(this, Observer {
            progressBar.visibility(it)
        })

        dataViewModel.empty.observe(this, Observer {emptyViewState ->
            if(ViewType.LAST_PAGE == emptyViewState.viewType) {
                isLastPage = true
            } else if(ViewType.NETWORK_ERROR == emptyViewState.viewType) {
                when(dataListingAdapter.list.isNotEmpty()) {
                    true -> {
                        Toast.makeText(this, emptyViewState.message, Toast.LENGTH_LONG).show()
                    }
                    false -> {
                        textViewError.text = emptyViewState.message
                        recyclerView.visibility = View.GONE
                        textViewError.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return ZoomHelper.getInstance().dispatchTouchEvent(ev!!,this) || super.dispatchTouchEvent(ev)
    }
}