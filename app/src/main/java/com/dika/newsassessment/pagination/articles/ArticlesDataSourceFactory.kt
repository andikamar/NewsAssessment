package com.dika.newsassessment.pagination.articles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.DataSource
import com.dika.newsassessment.models.NewsItem
import com.dika.newsassessment.helper.DataStatus

class ArticlesDataSourceFactory : DataSource.Factory<Int,NewsItem>(){
    val articlesLiveDataSource = MutableLiveData<ArticlesDataSource>()
    private var mQuery = "news"
    val dataStatusLiveData: LiveData<DataStatus> = Transformations
        .switchMap(articlesLiveDataSource, ArticlesDataSource::dataStatusMutableLiveData)

    override fun create(): DataSource<Int, NewsItem> {
        val itemDataSource = ArticlesDataSource(mQuery)
        articlesLiveDataSource.postValue(itemDataSource)
        return itemDataSource
    }

    fun setQuery(query: String) {
        mQuery = query
    }

}