package com.dika.newsassessment.models

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import com.google.gson.annotations.SerializedName

open class NewsItem : Parcelable {
    @SerializedName("source")
    var source: Source? = null

    @SerializedName("author")
    var author: String? = null

    @SerializedName("title")
    var title: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("url")
    var url: String? = null

    @SerializedName("urlToImage")
    var urlToImage: String? = null

    @SerializedName("publishedAt")
    var publishedAt: String? = null

    @SerializedName("content")
    var content: String? = null

    protected constructor(`in`: Parcel) {
        author = `in`.readString()
        title = `in`.readString()
        description = `in`.readString()
        url = `in`.readString()
        urlToImage = `in`.readString()
        publishedAt = `in`.readString()
        content = `in`.readString()
    }

    constructor()

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(author)
        dest.writeString(title)
        dest.writeString(description)
        dest.writeString(url)
        dest.writeString(urlToImage)
        dest.writeString(publishedAt)
        dest.writeString(content)
    }

    companion object {
        @JvmField
        val CREATOR: Creator<NewsItem?> = object : Creator<NewsItem?> {
            override fun createFromParcel(`in`: Parcel): NewsItem {
                return NewsItem(`in`)
            }

            override fun newArray(size: Int): Array<NewsItem?> {
                return arrayOfNulls(size)
            }
        }
    }
}
