package com.dika.newsassessment.view.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.dika.newsassessment.view.NewsDetailActivity
import com.dika.newsassessment.R
import com.dika.newsassessment.models.NewsItem
import com.dika.newsassessment.helper.DiffUtilCallBack
import com.dika.newsassessment.helper.Utils


class NewsItemAdapter : PagedListAdapter<NewsItem, NewsItemAdapter.MyViewHolder>(DiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_list_item, parent, false)
        return MyViewHolder(v)
    }

    @SuppressLint("CheckResult", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val newsItem = getItem(position)
        if (newsItem != null){
            val requestOptions = RequestOptions()
            requestOptions.placeholder(Utils.randomDrawableColor)
            requestOptions.error(Utils.randomDrawableColor)
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
            requestOptions.centerCrop()

            Glide.with(holder.itemView.context)
                .load(newsItem.urlToImage)
                .apply(requestOptions)
                .listener(object : RequestListener<Drawable> {
                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        holder.progressBarInImage.visibility = View.GONE
                        return false
                    }

                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        holder.progressBarInImage.visibility = View.GONE
                        return false
                    }
                })
                .into(holder.titleImage)

            holder.textViewTitle.text = newsItem.title
            holder.textViewDescription.text = newsItem.description
            holder.textViewSource.text = newsItem.source?.name
            holder.textViewTime.text = " \u2022 " + Utils.datetotimeFormat(newsItem.publishedAt.toString())
            holder.textViewPublishedAt.text = Utils.dateFormat(newsItem.publishedAt.toString())
            holder.textViewAuthor.text = newsItem.author

            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, NewsDetailActivity::class.java)
                intent.putExtra("source", newsItem.source!!.name)
                intent.putExtra("selected_article", newsItem)

                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    (holder.itemView.context as Activity?)!!,
                    holder.titleImage,
                    ViewCompat.getTransitionName(holder.titleImage)!!
                )

                holder.itemView.context.startActivity(intent, options.toBundle())
            }
        }
    }

    class MyViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val titleImage: ImageView = itemView.findViewById(R.id.image_view_title)
        val textViewAuthor: TextView = itemView.findViewById(R.id.text_view_source_author_time)
        val textViewTitle: TextView = itemView.findViewById(R.id.text_view_news_title)
        val textViewDescription: TextView = itemView.findViewById(R.id.text_view_news_description)
        val textViewSource: TextView = itemView.findViewById(R.id.text_view_source)
        val textViewTime: TextView = itemView.findViewById(R.id.text_view_time)
        val textViewPublishedAt: TextView = itemView.findViewById(R.id.text_view_publishedAt)
        val progressBarInImage: ProgressBar = itemView.findViewById(R.id.progress_bar_image)
    }
}