package com.dika.newsassessment.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.*
import com.dika.newsassessment.models.NewsItem
import com.dika.newsassessment.pagination.articles.ArticlesDataSource
import com.dika.newsassessment.pagination.articles.ArticlesDataSourceFactory
import com.dika.newsassessment.helper.DataStatus

class ArticlesViewModel : ViewModel() {
    var itemPagedList: LiveData<PagedList<NewsItem>>
    private var liveDataSource: MutableLiveData<ArticlesDataSource>
    private var articlesDataSourceFactory: ArticlesDataSourceFactory
    private var dataStatus: LiveData<*>
    fun setKeyword(query: String) {
        articlesDataSourceFactory.setQuery(query)
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
        articlesDataSourceFactory = ArticlesDataSourceFactory()
        liveDataSource = articlesDataSourceFactory.articlesLiveDataSource
        dataStatus = articlesDataSourceFactory.dataStatusLiveData
        val pagedListConfig: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(10).build()
        itemPagedList = LivePagedListBuilder(articlesDataSourceFactory, pagedListConfig).build()
    }
}