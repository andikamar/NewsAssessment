package com.dika.newsassessment.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.*
import com.dika.newsassessment.models.NewsItem
import com.dika.newsassessment.pagination.news.NewsDataSource
import com.dika.newsassessment.pagination.news.NewsDataSourceFactory
import com.dika.newsassessment.helper.DataStatus

class NewsViewModel : ViewModel() {
    var itemPagedList: LiveData<PagedList<NewsItem>>
    private val liveDataSource: MutableLiveData<NewsDataSource>
    private val newsDataSourceFactory: NewsDataSourceFactory = NewsDataSourceFactory()
    private val dataStatus: LiveData<*>
    fun setKeyword(query: String) {
        newsDataSourceFactory.setQuery(query)
        refreshData()
    }

    private fun refreshData() {
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