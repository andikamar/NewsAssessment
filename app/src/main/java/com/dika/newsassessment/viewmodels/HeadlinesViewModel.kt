package com.dika.newsassessment.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.*
import com.dika.newsassessment.models.NewsItem
import com.dika.newsassessment.pagination.headlines.HeadlinesDataSource
import com.dika.newsassessment.pagination.headlines.HeadlinesDataSourceFactory
import com.dika.newsassessment.helper.DataStatus

class HeadlinesViewModel : ViewModel() {
    var itemPagedList: LiveData<PagedList<NewsItem>>
    private val liveDataSource: MutableLiveData<HeadlinesDataSource>
    private val newsDataSourceFactory: HeadlinesDataSourceFactory = HeadlinesDataSourceFactory()
    private val dataStatus: LiveData<*>
    fun setCategory(category: String) {
        newsDataSourceFactory.setCategory(category)
        refreshData()
    }

    fun refreshData() {
        if (itemPagedList.value != null) {
            itemPagedList.value!!.dataSource.invalidate()
        }
    }

    fun getDataStatus(): LiveData<DataStatus> {
        return dataStatus as LiveData<DataStatus>
    }

    init {
        liveDataSource = newsDataSourceFactory.newsLiveDataSource
        dataStatus = newsDataSourceFactory.dataStatusLiveData
        val pagedListConfig: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(10).build()
        itemPagedList = LivePagedListBuilder(newsDataSourceFactory, pagedListConfig).build()
    }
}