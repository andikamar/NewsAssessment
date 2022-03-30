package com.dika.newsassessment.view

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.dika.newsassessment.R
import com.dika.newsassessment.view.fragments.ArticlesFragment
import com.dika.newsassessment.view.fragments.HeadlinesFragment
import com.dika.newsassessment.view.fragments.NewsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private var bottomNavigationView: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, NewsFragment())
                .setReorderingAllowed(true)
                .commit()
        }
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView?.setOnItemSelectedListener{ item: MenuItem ->
            when (item.itemId) {
                R.id.page_news -> supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, NewsFragment())
                    .setReorderingAllowed(true)
                    .commit()
                R.id.page_headlines -> supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, HeadlinesFragment())
                    .setReorderingAllowed(true)
                    .commit()
                R.id.page_articles -> supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, ArticlesFragment())
                    .setReorderingAllowed(true)
                    .commit()
            }
            return@setOnItemSelectedListener true
        }
    }
}