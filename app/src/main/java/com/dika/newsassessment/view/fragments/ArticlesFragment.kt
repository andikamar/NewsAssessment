package com.dika.newsassessment.view.fragments

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dika.newsassessment.R
import com.dika.newsassessment.view.adapters.NewsItemAdapter
import com.dika.newsassessment.helper.DataStatus
import com.dika.newsassessment.viewmodels.ArticlesViewModel

class ArticlesFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NewsItemAdapter
    private lateinit var emptyStateTextView: TextView
    private lateinit var textViewTitle: TextView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var keyword = ""
    private lateinit var articlesViewModel: ArticlesViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().title = "Articles"
        keyword = "news"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.fragment_articles, container, false)
        emptyStateTextView = rootView.findViewById(R.id.empty_view)
        swipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh)
        textViewTitle = rootView.findViewById(R.id.text_view_top_headlines)
        recyclerView = rootView.findViewById(R.id.recycler_view)
        if (savedInstanceState != null) {
            keyword = savedInstanceState.getString("keyword").toString()
        }
        initEmptyRecyclerView()
        articlesViewModel = ViewModelProvider(requireActivity()).get(ArticlesViewModel::class.java)
        subscribeObservers()
        swipeRefreshLayout.setOnRefreshListener {
            articlesViewModel.setKeyword(
                keyword
            )
        }
        setHasOptionsMenu(true)
        return rootView
    }

    private fun subscribeObservers() {
        articlesViewModel.itemPagedList.observe(
            viewLifecycleOwner
        ) { newsItems -> adapter.submitList(newsItems) }
        articlesViewModel.getDataStatus().observe(viewLifecycleOwner) { dataStatus ->
            when (dataStatus) {
                DataStatus.LOADED -> {
                    emptyStateTextView.visibility = View.INVISIBLE
                    swipeRefreshLayout.isRefreshing = false
                    textViewTitle.visibility = View.VISIBLE
                }
                DataStatus.LOADING -> {
                    swipeRefreshLayout.isRefreshing = true
                    textViewTitle.visibility = View.INVISIBLE
                    emptyStateTextView.visibility = View.INVISIBLE
                }
                DataStatus.EMPTY -> {
                    swipeRefreshLayout.isRefreshing = false
                    textViewTitle.visibility = View.INVISIBLE
                    emptyStateTextView.visibility = View.VISIBLE
                    emptyStateTextView.text = getString(R.string.no_news_found)
                }
                DataStatus.ERROR -> {
                    swipeRefreshLayout.isRefreshing = false
                    textViewTitle.visibility = View.INVISIBLE
                    emptyStateTextView.visibility = View.VISIBLE
                    emptyStateTextView.text = getString(R.string.no_internet_connection)
                }
            }
        }
    }
    private fun initEmptyRecyclerView() {
        adapter = NewsItemAdapter()
        recyclerView.adapter = adapter
        val linearLayoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        searchKeywordFromSearchView(menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun searchKeywordFromSearchView(menu: Menu) {
        val searchManager: SearchManager =
            requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        val searchMenuItem = menu.findItem(R.id.action_search)
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchView.queryHint = getString(R.string.keyword_articles)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.length > 2) {
                    keyword = query
                    swipeRefreshLayout.isRefreshing = true
                    textViewTitle.visibility = View.INVISIBLE
                    articlesViewModel.setKeyword(query)
                } else {
                    Toast.makeText(requireActivity(), "Type more than two letters!", Toast.LENGTH_SHORT)
                        .show()
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) {
                    keyword = "news"
                    articlesViewModel.setKeyword(keyword)
                }
                return false
            }
        })
        MenuItemCompat.setOnActionExpandListener(
            searchMenuItem,
            object : MenuItemCompat.OnActionExpandListener {
                override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                    return true
                }

                override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                    keyword = "news"
                    return true
                }
            })
        searchMenuItem.icon.setVisible(false, false)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("keyword", keyword)
    }
}