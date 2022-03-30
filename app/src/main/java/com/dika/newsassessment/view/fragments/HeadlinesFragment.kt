package com.dika.newsassessment.view.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dika.newsassessment.R
import com.dika.newsassessment.view.adapters.NewsItemAdapter
import com.dika.newsassessment.helper.DataStatus
import com.dika.newsassessment.viewmodels.HeadlinesViewModel
import com.google.android.material.card.MaterialCardView
import java.util.*

class HeadlinesFragment : Fragment(){
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NewsItemAdapter
    private lateinit var emptyStateTextView: TextView
    private lateinit var textViewTitle: TextView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var category = ""
    private lateinit var spinner: Spinner
    private lateinit var cardView: MaterialCardView
    private lateinit var mHeadlinesViewModel: HeadlinesViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().title = "Headlines"
        category = "business"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.fragment_headlines, container, false)
        emptyStateTextView = rootView.findViewById(R.id.empty_view)
        swipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh)
        textViewTitle = rootView.findViewById(R.id.text_view_top_headlines)
        recyclerView = rootView.findViewById(R.id.recycler_view)
        spinner = rootView.findViewById(R.id.spinner_category)
        cardView = rootView.findViewById(R.id.card_view)
        adapter = NewsItemAdapter()
        initSpinner()
        initEmptyRecyclerView()
        if (savedInstanceState != null) {
            category = savedInstanceState.getString("category").toString()
        }
        mHeadlinesViewModel = ViewModelProvider(requireActivity()).get(HeadlinesViewModel::class.java)

        subscribeObservers()
        swipeRefreshLayout.setOnRefreshListener {
            mHeadlinesViewModel.setCategory(category)
        }
        return rootView
    }

    private fun subscribeObservers() {
        mHeadlinesViewModel.itemPagedList.observe(
            viewLifecycleOwner
        ) { newsItems -> adapter.submitList(newsItems) }
        mHeadlinesViewModel.getDataStatus()
            .observe(viewLifecycleOwner) { dataStatus ->
                when (dataStatus) {
                    DataStatus.LOADED -> {
                        emptyStateTextView.visibility = View.INVISIBLE
                        swipeRefreshLayout.isRefreshing = false
                        textViewTitle.visibility = View.VISIBLE
                        cardView.visibility = View.VISIBLE
                        spinner.visibility = View.VISIBLE
                    }
                    DataStatus.LOADING -> {
                        swipeRefreshLayout.isRefreshing = true
                        emptyStateTextView.visibility = View.INVISIBLE
                        textViewTitle.visibility = View.VISIBLE
                        cardView.visibility = View.VISIBLE
                        spinner.visibility = View.VISIBLE
                    }
                    DataStatus.EMPTY -> {
                        swipeRefreshLayout.isRefreshing = false
                        textViewTitle.visibility = View.VISIBLE
                        cardView.visibility = View.VISIBLE
                        spinner.visibility = View.VISIBLE
                        emptyStateTextView.visibility = View.VISIBLE
                        emptyStateTextView.text = getString(R.string.no_news_found)
                    }
                    DataStatus.ERROR -> {
                        swipeRefreshLayout.isRefreshing = false
                        textViewTitle.visibility = View.VISIBLE
                        cardView.visibility = View.VISIBLE
                        spinner.visibility = View.VISIBLE
                        emptyStateTextView.visibility = View.VISIBLE
                        emptyStateTextView.text = getString(R.string.no_internet_connection)
                    }
                }
            }
    }

    private fun initSpinner() {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                category = parent?.getItemAtPosition(position).toString().lowercase(Locale.getDefault())
                mHeadlinesViewModel.setCategory(category)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.categories_array, android.R.layout.simple_spinner_item
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun initEmptyRecyclerView() {
        adapter = NewsItemAdapter()
        recyclerView.adapter = adapter
        val linearLayoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("category", category)
    }
}