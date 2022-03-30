package com.dika.newsassessment.helper

import androidx.recyclerview.widget.DiffUtil
import com.dika.newsassessment.models.NewsItem

class DiffUtilCallBack : DiffUtil.ItemCallback<NewsItem>() {
    override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
        return oldItem.url.equals(newItem.url)
    }

    override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
        return oldItem.title == newItem.title
                && oldItem.description == newItem.description
                && oldItem.author == newItem.author
    }

}