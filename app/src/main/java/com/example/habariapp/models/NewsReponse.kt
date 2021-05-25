package com.androiddevs.NewsApp.api.models

data class NewsReponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)