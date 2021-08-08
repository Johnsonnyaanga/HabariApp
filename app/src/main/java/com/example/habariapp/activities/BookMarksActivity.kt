package com.example.habariapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habariapp.MainActivity
import com.example.habariapp.R
import com.example.habariapp.adapters.NewsAdapter
import com.example.habariapp.database.NewsDatabase
import com.example.habariapp.repository.NewsRepository
import com.example.habariapp.ui.NewsViewModel
import com.example.habariapp.ui.NewsViewModelFactoryProvider
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_book_marks.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_saved_news.*
import kotlinx.android.synthetic.main.fragment_saved_news.rvSavedNews

class BookMarksActivity : AppCompatActivity() {
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_marks)

        //back naviagtion
        back_bookmarks.setOnClickListener(View.OnClickListener {
            finish()
        })

        val newsRepository = NewsRepository(NewsDatabase(this))
        val newsViewModelFactoryProvider = NewsViewModelFactoryProvider(application, newsRepository)
        viewModel = ViewModelProvider(this, newsViewModelFactoryProvider).get(NewsViewModel::class.java)
        //setSupportActionBar(toolbar_bookmarks)

        setupRecyclerView()

        newsAdapter.setOnItemClickListener {

            val intent = Intent(this, ArticleActivity::class.java)
            intent.putExtra("article", it.url)
            startActivity(intent)

            /*    val bundle = Bundle().apply {
                    putSerializable("article", it)
                }
                findNavController().navigate(
                        R.id.action_savedNewsFragment_to_articleFragment,
                        bundle
                )*/
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]
                viewModel.deleteArticle(article)
                Snackbar.make(parentLayout, "Successfully deleted article", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        viewModel.saveArticle(article)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(rvSavedNews)
        }

        viewModel.getSavedNews().observe(this as LifecycleOwner, Observer { articles ->
            newsAdapter.differ.submitList(articles)
        })
    }
    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        rvSavedNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(this@BookMarksActivity)
        }
    }
}