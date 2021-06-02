package com.example.habariapp.repository

import com.androiddevs.NewsApp.api.RetrofitInstance
import com.example.habariapp.database.NewsDatabase

class NewsRepository(
    val db:NewsDatabase
) {
    suspend fun getBreakingNews(countryCode:String,pageNumber:Int) =
        RetrofitInstance.api.getBreakingNews(countryCode,pageNumber)
    suspend fun searchNews(searchQuery:String,pageNumber:Int) =
        RetrofitInstance.api.searchNews(searchQuery,pageNumber)

}