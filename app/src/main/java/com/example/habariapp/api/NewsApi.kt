package com.androiddevs.NewsApp.api

import com.androiddevs.NewsApp.api.models.NewsResponse
import com.androiddevs.NewsApp.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface
NewsApi {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
            @Query("country") countryCode: String = "us",
            @Query("page") pageNumber: Int = 1,
            @Query("apiKey") apiKey: String = API_KEY,

            ): Response<NewsResponse>

    @GET("v2/top-headlines")
    suspend fun getSportsNews(
            @Query("country") countryCode: String = "us",
            @Query("page") pageNumber: Int = 1,
            @Query("apiKey") apiKey: String = API_KEY,
            @Query("category") categoryString: String = "sports"
    ): Response<NewsResponse>

    @GET("v2/top-headlines")
    suspend fun getEntertainmentNews(
            @Query("country") countryCode: String = "us",
            @Query("page") pageNumber: Int = 1,
            @Query("apiKey") apiKey: String = API_KEY,
            @Query("category") categoryString: String = "entertainment"
    ): Response<NewsResponse>

    @GET("v2/top-headlines")
    suspend fun getHealthNews(
            @Query("country") countryCode: String = "us",
            @Query("page") pageNumber: Int = 1,
            @Query("apiKey") apiKey: String = API_KEY,
            @Query("category") categoryString: String = "health"
    ): Response<NewsResponse>

    @GET("v2/top-headlines")
    suspend fun getTechnologyNews(
            @Query("country") countryCode: String = "us",
            @Query("page") pageNumber: Int = 1,
            @Query("apiKey") apiKey: String = API_KEY,
            @Query("category") categoryString: String = "technology"
    ): Response<NewsResponse>

    @GET("v2/top-headlines")
    suspend fun searchNews(
            @Query("q") searchQuery: String,
            @Query("page") pageNumber: Int = 1,
            @Query("apiKey") apiKey: String = API_KEY
    ): Response<NewsResponse>


    @GET("v2/top-headlines")
    suspend fun cryptoNews(
            @Query("q") cryptoQuery: String = "CryptoCurrency",
            @Query("page") pageNumber: Int = 1,
            @Query("apiKey") apiKey: String = API_KEY
    ): Response<NewsResponse>


}