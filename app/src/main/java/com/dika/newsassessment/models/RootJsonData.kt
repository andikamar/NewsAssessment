package com.dika.newsassessment.models

import com.google.gson.annotations.SerializedName

class RootJsonData {
    @SerializedName("status")
    val status: String? = null

    @SerializedName("totalResults")
    val totalResults = 0

    @SerializedName("articles")
    val newsItems: List<NewsItem>? = null
}
