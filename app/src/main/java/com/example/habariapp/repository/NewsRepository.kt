package com.example.habariapp.repository

import com.androiddevs.NewsApp.api.RetrofitInstance
import com.androiddevs.NewsApp.api.models.Article
import com.example.habariapp.database.NewsDatabase

class NewsRepository(
        val db: NewsDatabase
) {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
            RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun getSportsNews(countryCode: String, pageNumber: Int) =
            RetrofitInstance.api.getSportsNews(countryCode, pageNumber)

    suspend fun getEntertainmentNews(countryCode: String, pageNumber: Int) =
            RetrofitInstance.api.getEntertainmentNews(countryCode, pageNumber)

    suspend fun getHealthNews(countryCode: String, pageNumber: Int) =
            RetrofitInstance.api.getHealthNews(countryCode, pageNumber)

    suspend fun getTechnologyNews(countryCode: String, pageNumber: Int) =
            RetrofitInstance.api.getTechnologyNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
            RetrofitInstance.api.searchNews(searchQuery, pageNumber)


    suspend fun insertArticle(article: Article)
    = db.getArticlesDao().insertArticle(article)

    suspend fun deleteArticle(article: Article)
    = db.getArticlesDao().deleteArticle(article)

    fun getAllArticles() = db.getArticlesDao().getAllArticles()
    suspend fun deleteAllArticles() {
        db.getArticlesDao().deleteAllCachedArticles()
    }


}