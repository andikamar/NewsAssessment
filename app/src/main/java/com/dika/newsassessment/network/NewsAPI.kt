package com.dika.newsassessment.network

import com.dika.newsassessment.models.RootJsonData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {
    @GET("top-headlines")
    fun getTopHeadlinesByLanguage(
        @Query("language") language: String,
        @Query("apiKey") apiKey: String,
        @Query("page") pageNumber: Int,
        @Query("pageSize") pageSize: Int
    ): Call<RootJsonData>

    @GET("top-headlines")
    fun getTopHeadlinesByCategory(
        @Query("category") category: String,
        @Query("language") language: String,
        @Query("apiKey") apiKey: String,
        @Query("page") pageNumber: Int,
        @Query("pageSize") pageSize: Int
    ): Call<RootJsonData>

    @GET("top-headlines")
    fun searchNewsByKeyWord(
        @Query("q") keyword: String,
        @Query("sortBy") sortBy: String,
        @Query("language") language: String,
        @Query("apiKey") apiKey: String,
        @Query("page") pageNumber: Int,
        @Query("pageSize") pageSize: Int
    ): Call<RootJsonData>

    @GET("everything")
    fun searchArticlesByKeyWord(
        @Query("q") keyword: String,
        @Query("sortBy") sortBy: String,
        @Query("language") language: String,
        @Query("apiKey") apiKey: String,
        @Query("page") pageNumber: Int,
        @Query("pageSize") pageSize: Int
    ): Call<RootJsonData>
}
