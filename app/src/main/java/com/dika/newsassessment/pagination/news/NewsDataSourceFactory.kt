package com.dika.newsassessment.pagination.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.DataSource
import com.dika.newsassessment.models.NewsItem
import com.dika.newsassessment.helper.DataStatus

class NewsDataSourceFactory : DataSource.Factory<Int, NewsItem>(){
    val newsLiveDataSource = MutableLiveData<NewsDataSource>()
    private var mQuery = ""
    val dataStatusLiveData: LiveData<DataStatus> = Transformations
        .switchMap(newsLiveDataSource, NewsDataSource::getDataStatusMutableLiveData)

    override fun create(): DataSource<Int, NewsItem> {
        val itemDataSource = NewsDataSource(mQuery)
        newsLiveDataSource.postValue(itemDataSource)
        return itemDataSource
    }

    fun setQuery(query: String) {
        mQuery = query
    }

}