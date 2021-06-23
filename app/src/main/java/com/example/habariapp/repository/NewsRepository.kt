package com.example.habariapp.repository

import com.androiddevs.NewsApp.api.RetrofitInstance
import com.androiddevs.NewsApp.api.models.Article
import com.example.habariapp.database.NewsDatabase

class NewsRepository(
    val db:NewsDatabase
) {
    suspend fun getBreakingNews(countryCode:String,pageNumber:Int) =
        RetrofitInstance.api.getBreakingNews(countryCode,pageNumber)
    suspend fun searchNews(searchQuery:String,pageNumber:Int) =
        RetrofitInstance.api.searchNews(searchQuery,pageNumber)
    suspend fun insertArticle(article: Article) = db.getArticlesDao().insertArticle(article)
    suspend fun deleteArticle(article: Article) = db.getArticlesDao().deleteArticle(article)
    fun getAllArticles() = db.getArticlesDao().getAllArticles()

    suspend fun getCryptoNews(pageNumber: Int) =
        RetrofitInstance.api.cryptoNews(pageNumber = pageNumber)
    suspend fun sportsNews(pageNumber: Int) =
        RetrofitInstance.api.sPortsNews(pageNumber = pageNumber)

}