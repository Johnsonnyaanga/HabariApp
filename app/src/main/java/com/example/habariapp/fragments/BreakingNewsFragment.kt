package com.example.habariapp.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.NewsApp.util.Constants.Companion.QUERY_PAGE_SIZE
import com.example.habariapp.MainActivity
import com.example.habariapp.activities.ArticleActivity
import com.example.habariapp.R
import com.example.habariapp.adapters.NewsAdapter
import com.example.habariapp.ui.NewsViewModel
import com.example.habariapp.util.ErrorsAndWarnings
import com.example.habariapp.util.InternetCheck
import com.example.habariapp.util.Resource
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import java.io.IOException

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    val TAG = "BreakingNewsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()





        newsAdapter.setOnItemClickListener {
            val intent = Intent(requireActivity(), ArticleActivity::class.java)
            intent.putExtra("article", it.url)
            startActivity(intent)
        /*    val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                    R.id.action_breakingNewsFragment_to_articleFragment,
                    bundle
            )*/
        }


        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success -> {

                    Log.d("datar",response.data?.articles.toString())
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.breakingNewsPage == totalPages
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        ErrorsAndWarnings(requireContext()).toastMessageLong("an Error Occured: $message")
                        no_net_txt.visibility = VISIBLE
                        retry_btn.visibility = VISIBLE


                    }
                }
                is Resource.Loading -> {
                    no_net_txt.visibility = GONE
                    retry_btn.visibility = GONE
                    showProgressBar()
                }
            }
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
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if(shouldPaginate) {
                viewModel.getBreakingNews("us")
                isScrolling = false
            } else {
                rvBreakingNews.setPadding(0, 0, 0, 0)
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
        rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@BreakingNewsFragment.scrollListener)
        }
    }

    /*fun checkInternet(){
        if(isConnected()){
            yes_net.visibility = VISIBLE
            no_net.visibility = GONE
        }else{
            no_net.visibility = VISIBLE
            yes_net.visibility = GONE
            Toast.makeText(requireActivity(),"There is no internet connection, try again",Toast.LENGTH_SHORT).show()
        }
    }*/

    override fun onResume() {
        super.onResume()
        retry_btn.setOnClickListener(View.OnClickListener {
            viewModel.getBreakingNews("us")
        })
    }


    @Throws(InterruptedException::class, IOException::class)
    fun isConnected(): Boolean {
        val command = "ping -c 1 google.com"
        return Runtime.getRuntime().exec(command).waitFor() == 0
    }


    val internetCheck = activity?.application?.let { InternetCheck(it) }

   /* override fun onResume() {
        super.onResume()
        checkInternet()
        materialButton.setOnClickListener(View.OnClickListener {
            checkInternet()
        })
    }*/
}