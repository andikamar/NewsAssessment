package com.dika.newsassessment.view

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.dika.newsassessment.R
import com.dika.newsassessment.models.NewsItem
import com.dika.newsassessment.models.Source
import com.dika.newsassessment.helper.Utils
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import kotlin.math.abs


class NewsDetailActivity : AppCompatActivity(), AppBarLayout.OnOffsetChangedListener {
    private lateinit var toolbar: Toolbar
    private lateinit var titleImageView: ImageView
    private lateinit var appbarTitleTextView: TextView
    private lateinit var appbarSubtitleTextView: TextView
    private lateinit var titleTextView: TextView
    private lateinit var authorTextView: TextView
    private lateinit var datePublishedTextView: TextView
    private lateinit var mUrl: String
    private lateinit var mUrlToImage: String
    private lateinit var mTitle: String
    private lateinit var mDate: String
    private lateinit var mSource: String
    private lateinit var mAuthor: String
    private lateinit var mDescription: String
    private var isHideToolbarView = false
    private lateinit var titleAppbar: LinearLayout
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var frameLayoutDateBehavior: FrameLayout
    private lateinit var newsItem: NewsItem


    @SuppressLint("CheckResult", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            title = ""
            setDisplayShowHomeEnabled(true)
        }
        val collapsingToolbarLayout: CollapsingToolbarLayout =
            findViewById(R.id.collapsing_toolbar)
        collapsingToolbarLayout.title = ""
        appBarLayout = findViewById(R.id.layout_appbar)
        appBarLayout.addOnOffsetChangedListener(this)
        frameLayoutDateBehavior = findViewById(R.id.date_behavior)
        titleAppbar = findViewById(R.id.layout_title_appbar)
        titleImageView = findViewById(R.id.image_view_title)
        appbarTitleTextView = findViewById(R.id.title_on_layout_title_appbar)
        appbarSubtitleTextView = findViewById(R.id.subtitle_on_layout_title_appbar)
        authorTextView = findViewById(R.id.text_view_source_author_time)
        titleTextView = findViewById(R.id.text_view_title_news)
        datePublishedTextView = findViewById(R.id.text_view_date_published)
        val intent: Intent = intent
        newsItem = intent.getParcelableExtra("selected_article")!!

        newsItem.source = Source(intent.getStringExtra("source")!!)
        mSource = newsItem.source!!.name
        mUrl = newsItem.url.toString()
        mUrlToImage = newsItem.urlToImage.toString()
        mTitle = newsItem.title.toString()
        mDate = newsItem.publishedAt.toString()
        mAuthor = newsItem.author.toString()
        mDescription = newsItem.description.toString()

        val requestOptions = RequestOptions()
        requestOptions.error(Utils.randomDrawableColor)
        Glide.with(this)
            .load(mUrlToImage)
            .apply(requestOptions)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(titleImageView)
        appbarTitleTextView.text = mSource
        appbarSubtitleTextView.text = mUrl
        titleTextView.text = mTitle
        authorTextView.text = mSource + appendAuthorWithBullet()
        datePublishedTextView.text = Utils.dateFormat(mDate)
        initWebView(mUrl)
    }

    private fun appendAuthorWithBullet(): String {
        return " \u2022 $mAuthor"
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView(url: String) {
        val webView: WebView = findViewById(R.id.web_view)
        webView.settings.loadsImagesAutomatically = true
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.setSupportZoom(true)
        webView.settings.builtInZoomControls = true
        webView.settings.displayZoomControls = false
        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        webView.webViewClient = WebViewClient()
        webView.loadUrl(url)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        supportFinishAfterTransition()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        val maxScroll: Int = appBarLayout.totalScrollRange
        val percentage = abs(verticalOffset).toFloat() / maxScroll.toFloat()
        if (percentage == 1f && isHideToolbarView) {
            frameLayoutDateBehavior.visibility = View.GONE
            titleAppbar.visibility = View.VISIBLE
            isHideToolbarView = !isHideToolbarView
        } else if (percentage < 1f && !isHideToolbarView) {
            frameLayoutDateBehavior.visibility = View.VISIBLE
            titleAppbar.visibility = View.GONE
            isHideToolbarView = !isHideToolbarView
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_news, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.open_in_browser) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(mUrl)
            startActivity(intent)
            return true
        } else if (id == R.id.share) {
            try {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plan"
                intent.putExtra(Intent.EXTRA_SUBJECT, mSource)
                val body = "$mTitle\n$mUrl\nVia News App.\n"
                intent.putExtra(Intent.EXTRA_TEXT, body)
                startActivity(Intent.createChooser(intent, "Share with :"))
            } catch (e: Exception) {
                Toast.makeText(this, "Oops! \nCannot share this news", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}