package com.dika.newsassessment.pagination.articles

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.dika.newsassessment.models.NewsItem
import com.dika.newsassessment.models.RootJsonData
import com.dika.newsassessment.network.NewsAPI
import com.dika.newsassessment.network.ServiceGenerator
import com.dika.newsassessment.helper.DataStatus
import com.dika.newsassessment.helper.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticlesDataSource(private val mKeyword: String) : PageKeyedDataSource<Int, NewsItem>()  {
    val dataStatusMutableLiveData: MutableLiveData<DataStatus> = MutableLiveData()

    companion object {
        private const val FIRST_PAGE = 1
        const val SORT_ORDER = "publishedAt"
        const val LANGUAGE = "en"
        const val API_KEY: String = Utils.API_KEY
        const val PAGE_SIZE = 10
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, NewsItem>) {
        dataStatusMutableLiveData.postValue(DataStatus.LOADING)
        val newsAPI = ServiceGenerator.createService(NewsAPI::class.java)
        val call = newsAPI.searchArticlesByKeyWord(
            mKeyword,
            SORT_ORDER,
            LANGUAGE,
            API_KEY,
            FIRST_PAGE,
            PAGE_SIZE
        )
        call.enqueue(object : Callback<RootJsonData> {
            override fun onResponse(call: Call<RootJsonData>, response: Response<RootJsonData>) {
                if (response.body() != null) {
                    response.body()!!.newsItems?.let {
                        callback.onResult(it, null, FIRST_PAGE + 1)
                    }
                    dataStatusMutableLiveData.postValue(DataStatus.LOADED)
                }
                if (response.body()?.newsItems?.isEmpty() == true) {
                    dataStatusMutableLiveData.postValue(DataStatus.EMPTY)
                }
            }

            override fun onFailure(call: Call<RootJsonData>, t: Throwable) {
                dataStatusMutableLiveData.postValue(DataStatus.ERROR)
            }
        })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, NewsItem>) {
        val newsAPI = ServiceGenerator.createService(NewsAPI::class.java)
        val call = newsAPI.searchArticlesByKeyWord(
            mKeyword,
            SORT_ORDER,
            LANGUAGE,
            API_KEY,
            FIRST_PAGE,
            PAGE_SIZE
        )
        call.enqueue(object : Callback<RootJsonData> {
            override fun onResponse(call: Call<RootJsonData>, response: Response<RootJsonData>) {
                val adjacentKey = if (params.key > 1) params.key - 1 else null
                if (response.body() != null) {
                    response.body()!!.newsItems?.let { callback.onResult(it, adjacentKey) }
                }
            }

            override fun onFailure(call: Call<RootJsonData>, t: Throwable) {}
        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, NewsItem>) {
        val newsAPI = ServiceGenerator.createService(NewsAPI::class.java)
        val call = newsAPI.searchArticlesByKeyWord(
            mKeyword,
            SORT_ORDER,
            LANGUAGE,
            API_KEY,
            params.key,
            PAGE_SIZE
        )
        call.enqueue(object : Callback<RootJsonData> {
            override fun onResponse(call: Call<RootJsonData>, response: Response<RootJsonData>) {
                dataStatusMutableLiveData.postValue(DataStatus.LOADED)
                if (response.code() == 429) {
                    val emptyList: List<NewsItem> = ArrayList()
                    callback.onResult(emptyList, null)
                }
                if (response.body() != null) {
                    val key = params.key + 1

                    if (response.body()!!.newsItems!!.isNotEmpty()) {
                        response.body()!!.newsItems?.let {
                            callback.onResult(it, key) }
                    }
                }
            }

            override fun onFailure(call: Call<RootJsonData>, t: Throwable) {
                dataStatusMutableLiveData.postValue(DataStatus.ERROR)
            }
        })
    }
}
