package com.example.habariapp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.NewsApp.api.models.Article
import com.bumptech.glide.Glide
import com.example.habariapp.R
import kotlinx.android.synthetic.main.artcle_view.view.*

class NewsAdapter:RecyclerView.Adapter<NewsAdapter.ArticlesViewHolder>() {

    inner class ArticlesViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)

    private val differCallBack = object:DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
           return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem

        }
    }
    val differ = AsyncListDiffer(this,differCallBack)











    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlesViewHolder {
        return ArticlesViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.article_view_view,
                parent,
                false
            )
        )
    }
    private var onItemClickListener: ((Article) -> Unit)? = null

    override fun onBindViewHolder(holder: ArticlesViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(context).load(article.urlToImage).into(ivArticleImage)
            tvTitle.text = article.title
            //tvDescription.text = article.description
            tvPublishedAt.text = article.publishedAt
            //tvSource.text = article.author
            setOnClickListener {
                onItemClickListener?.let { it(article) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }


}