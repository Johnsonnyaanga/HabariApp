package com.example.habariapp.activities

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.example.habariapp.MainActivity
import com.example.habariapp.R
import com.example.habariapp.fragments.ArticleFragmentArgs
import com.example.habariapp.ui.NewsViewModel
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.fragment_article.*
import kotlinx.android.synthetic.main.fragment_article.webView


class ArticleActivity : AppCompatActivity() {
    lateinit var viewModel: NewsViewModel
    val args: ArticleFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        progress_webView.visibility = View.VISIBLE

        //val main = MainActivity()
        //viewModel = main.viewModel
        val article = intent.getStringExtra("article")
        webView.apply {
            //webViewClient = WebViewClient()
             loadUrl(article.toString())
        }

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)

            }
            override fun onPageFinished(view: WebView, url: String) {
                // do your stuff here
                progress_webView.visibility = View.GONE
            }

            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?
            ) {
                progress_webView.visibility = View.GONE
                Toast.makeText(this@ArticleActivity, "Could not load your page", Toast.LENGTH_SHORT).show()
                super.onReceivedError(view, errorCode, description, failingUrl)
                Toast.makeText(this@ArticleActivity, "error occured", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@ArticleActivity, MainActivity::class.java))
            }
        }

      /*  fab.setOnClickListener(View.OnClickListener {
            viewModel.saveArticle(article)
            Snackbar.make(findViewById(R.id.parent1),"Article saved succesifully", Snackbar.LENGTH_SHORT).show()
        })*/
    }
}