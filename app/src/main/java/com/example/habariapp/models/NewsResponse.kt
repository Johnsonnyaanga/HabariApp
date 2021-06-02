package com.androiddevs.NewsApp.api.models

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)