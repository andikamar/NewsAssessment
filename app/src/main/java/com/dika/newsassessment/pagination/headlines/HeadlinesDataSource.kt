package com.dika.newsassessment.pagination.headlines

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

class HeadlinesDataSource(private val mCategory: String) : PageKeyedDataSource<Int, NewsItem>() {
    var language = "en"
    private val dataStatusMutableLiveData: MutableLiveData<DataStatus> = MutableLiveData<DataStatus>()
    fun getDataStatusMutableLiveData(): MutableLiveData<DataStatus> {
        return dataStatusMutableLiveData
    }


    private fun createHeadlinesJsonDataCall(category: String, pageNumber: Int): Call<RootJsonData> {
        val newsAPI = ServiceGenerator.createService(NewsAPI::class.java)

        return newsAPI.getTopHeadlinesByCategory(category, language, API_KEY, pageNumber, PAGE_SIZE)
    }

    companion object {
        private const val FIRST_PAGE = 1
        const val API_KEY: String = Utils.API_KEY
        const val PAGE_SIZE = 10
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, NewsItem>) {
        dataStatusMutableLiveData.postValue(DataStatus.LOADING)
        val rootJsonDataCall: Call<RootJsonData> =
            createHeadlinesJsonDataCall(mCategory, FIRST_PAGE)
        rootJsonDataCall.enqueue(object : Callback<RootJsonData?> {
            override fun onResponse(call: Call<RootJsonData?>, response: Response<RootJsonData?>) {
                if (response.body() != null) {
                    response.body()!!.newsItems?.let { callback.onResult(it, null, FIRST_PAGE + 1) }
                    dataStatusMutableLiveData.postValue(DataStatus.LOADED)
                }
                if (response.body()?.totalResults == 0) {
                    dataStatusMutableLiveData.postValue(DataStatus.EMPTY)
                }
            }

            override fun onFailure(call: Call<RootJsonData?>, t: Throwable) {
                dataStatusMutableLiveData.postValue(DataStatus.ERROR)
            }
        })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, NewsItem>) {
        dataStatusMutableLiveData.postValue(DataStatus.LOADING)
        val rootJsonDataCall: Call<RootJsonData> =
            createHeadlinesJsonDataCall(mCategory, FIRST_PAGE)
        rootJsonDataCall.enqueue(object : Callback<RootJsonData?> {
            override fun onResponse(call: Call<RootJsonData?>, response: Response<RootJsonData?>) {
                val adjacentKey: Int? = if (params.key > 1) params.key - 1 else null
                if (response.body() != null) {
                    response.body()!!.newsItems?.let { callback.onResult(it, adjacentKey) }
                }
                dataStatusMutableLiveData.postValue(DataStatus.LOADED)
            }

            override fun onFailure(call: Call<RootJsonData?>, t: Throwable) {
                dataStatusMutableLiveData.postValue(DataStatus.ERROR)
            }
        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, NewsItem>) {
        val rootJsonDataCall: Call<RootJsonData> =
            createHeadlinesJsonDataCall(mCategory, params.key)
        rootJsonDataCall.enqueue(object : Callback<RootJsonData?> {
            override fun onResponse(call: Call<RootJsonData?>, response: Response<RootJsonData?>) {
                dataStatusMutableLiveData.postValue(DataStatus.LOADED)
                if (response.code() == 429) {
                    val emptyList: List<NewsItem> = ArrayList<NewsItem>()
                    callback.onResult(emptyList, null)
                }
                if (response.body() != null) {
                    val key: Int = params.key + 1

                    if (!response.body()!!.newsItems!!.isEmpty()) {
                        response.body()!!.newsItems?.let { callback.onResult(it, key) }
                    }
                }
            }

            override fun onFailure(call: Call<RootJsonData?>, t: Throwable) {
                dataStatusMutableLiveData.postValue(DataStatus.ERROR)
            }
        })
    }
}