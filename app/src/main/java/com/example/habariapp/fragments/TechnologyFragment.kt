package com.example.habariapp.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.AbsListView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.NewsApp.util.Constants
import com.example.habariapp.MainActivity
import com.example.habariapp.activities.ArticleActivity
import com.example.habariapp.R
import com.example.habariapp.adapters.NewsAdapter
import com.example.habariapp.ui.NewsViewModel
import com.example.habariapp.util.ErrorsAndWarnings
import com.example.habariapp.util.Resource
import kotlinx.android.synthetic.main.fragment_technology.*


class TechnologyFragment : Fragment(R.layout.fragment_technology) {
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    val TAG = "TechnologyNewsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            val intent = Intent(requireActivity(), ArticleActivity::class.java)
            intent.putExtra("article", it.url)
            startActivity(intent)
       /*     val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_technologyFragment_to_articleFragment,
                bundle
            )*/
        }

        viewModel.technologyNews.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    Log.d("datar",response.data?.articles.toString())
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.technologyNewsPage == totalPages
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        ErrorsAndWarnings(requireContext()).toastMessageLong("an Error Occured: $message")
                        tech_retry_btn.visibility = VISIBLE
                        tech_no_net_txt.visibility = VISIBLE
                    }
                }
                is Resource.Loading -> {
                    tech_retry_btn.visibility = GONE
                    tech_no_net_txt.visibility = GONE
                    showProgressBar()
                }
            }
        })



    }

    override fun onResume() {
        super.onResume()
        tech_retry_btn.setOnClickListener(View.OnClickListener {
            viewModel.getTechnologyNews("us")
        })
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if(shouldPaginate) {
                viewModel.getTechnologyNews("us")
                isScrolling = false
            } else {
                rvTechnologyNews.setPadding(0, 0, 0, 0)
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        rvTechnologyNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@TechnologyFragment.scrollListener)
        }
    }


}