package com.dika.newsassessment.pagination.headlines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.DataSource
import com.dika.newsassessment.models.NewsItem
import com.dika.newsassessment.helper.DataStatus

class HeadlinesDataSourceFactory : DataSource.Factory<Int,NewsItem>(){
    val newsLiveDataSource = MutableLiveData<HeadlinesDataSource>()
    private var mCategory = "business"
    val dataStatusLiveData: LiveData<DataStatus> = Transformations
        .switchMap(newsLiveDataSource, HeadlinesDataSource::getDataStatusMutableLiveData)

    override fun create(): DataSource<Int, NewsItem> {
        val itemDataSource = HeadlinesDataSource(mCategory)
        newsLiveDataSource.postValue(itemDataSource)
        return itemDataSource
    }

    fun setCategory(category: String) {
        mCategory = category
    }

}