package com.example.habariapp.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.NewsApp.api.models.Article
import com.androiddevs.NewsApp.api.models.NewsResponse
import com.example.habariapp.HabariApplication
import com.example.habariapp.repository.NewsRepository
import com.example.habariapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(
        application: Application,
        val newsRepository: NewsRepository
) : AndroidViewModel(application) {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    var breakingNewsResponse: NewsResponse? = null

    //sports news
    val sportsNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var sportsNewsPage = 1
    var sportsNewsResponse: NewsResponse? = null

    //entertainment news
    val entertainmentNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var entertainmentNewsPage = 1
    var entertainmentNewsResponse: NewsResponse? = null

    //health news
    val healthNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var healthNewsPage = 1
    var healthNewsResponse: NewsResponse? = null

    //technology news
    val technologyNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var technologyNewsPage = 1
    var technologyNewsResponse: NewsResponse? = null


    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null


    init {
        getBreakingNews("us")
        getSportsNews("us")
        getEntertainmentNews("us")
        getHealthNews("us")
        getTechnologyNews("us")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        safeBreakingNewsCall(countryCode)

    }

    fun getSportsNews(countryCode: String) = viewModelScope.launch {
        safeSportsNewsCall(countryCode)

    }

    fun getEntertainmentNews(countryCode: String) = viewModelScope.launch {
        safeEntertainmentNewsCall(countryCode)

    }

    fun getHealthNews(countryCode: String) = viewModelScope.launch {
        safeHealthNewsCall(countryCode)

    }

    fun getTechnologyNews(countryCode: String) = viewModelScope.launch {
        safeTechnologyNewsCall(countryCode)

    }


    fun searchNews(searchQuery: String) = viewModelScope.launch {
        safeSearchNewsCall(searchQuery)
    }


    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                breakingNewsPage++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = resultResponse
                } else {
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message(), null)
    }

    private fun handleSportsNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                sportsNewsPage++
                if (sportsNewsResponse == null) {
                    sportsNewsResponse = resultResponse
                } else {
                    val oldArticles = sportsNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(sportsNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message(), null)
    }

    private fun handleEntertainmentNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                entertainmentNewsPage++
                if (entertainmentNewsResponse == null) {
                    entertainmentNewsResponse = resultResponse
                } else {
                    val oldArticles = entertainmentNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(entertainmentNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message(), null)
    }

    private fun handleHealthNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                healthNewsPage++
                if (healthNewsResponse == null) {
                    healthNewsResponse = resultResponse
                } else {
                    val oldArticles = healthNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(healthNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message(), null)
    }

    private fun handleTechnologyNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                technologyNewsPage++
                if (technologyNewsResponse == null) {
                    technologyNewsResponse = resultResponse
                } else {
                    val oldArticles = technologyNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(technologyNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message(), null)
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchNewsPage++
                if (searchNewsResponse == null) {
                    searchNewsResponse = resultResponse
                } else {
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message(), null)
    }


    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.insertArticle(article)
    }

    fun getSavedNews() = newsRepository.getAllArticles()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    fun deleteAllArticles() {
        viewModelScope.launch {
            newsRepository.deleteAllArticles()
        }
    }

    private suspend fun safeBreakingNewsCall(countryCode: String) {
        breakingNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
                breakingNews.postValue(handleBreakingNewsResponse(response))

            } else {
                breakingNews.postValue(Resource.Error("No Internet Connection", null))
                safeBreakingNewsCall("us")
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> breakingNews.postValue(Resource.Error("NetworkFailure", null))
                else -> breakingNews.postValue(Resource.Error("Conversion Error", null))
            }

        }

    }

    private suspend fun safeSportsNewsCall(countryCode: String) {
        sportsNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepository.getSportsNews(countryCode, sportsNewsPage)
                sportsNews.postValue(handleSportsNewsResponse(response))

            } else {
                sportsNews.postValue(Resource.Error("No Internet Connection", null))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> sportsNews.postValue(Resource.Error("NetworkFailure", null))
                else -> sportsNews.postValue(Resource.Error("Conversion Error", null))
            }

        }

    }

    private suspend fun safeHealthNewsCall(countryCode: String) {
        healthNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepository.getHealthNews(countryCode, healthNewsPage)
                healthNews.postValue(handleHealthNewsResponse(response))

            } else {
                healthNews.postValue(Resource.Error("No Internet Connection", null))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> healthNews.postValue(Resource.Error("NetworkFailure", null))
                else -> healthNews.postValue(Resource.Error("Conversion Error", null))
            }

        }

    }

    private suspend fun safeTechnologyNewsCall(countryCode: String) {
        technologyNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepository.getTechnologyNews(countryCode, technologyNewsPage)
                technologyNews.postValue(handleTechnologyNewsResponse(response))

            } else {
                technologyNews.postValue(Resource.Error("No Internet Connection", null))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> technologyNews.postValue(Resource.Error("NetworkFailure", null))
                else -> technologyNews.postValue(Resource.Error("Conversion Error", null))
            }

        }

    }

    private suspend fun safeEntertainmentNewsCall(countryCode: String) {
        entertainmentNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepository.getEntertainmentNews(countryCode, entertainmentNewsPage)
                entertainmentNews.postValue(handleEntertainmentNewsResponse(response))

            } else {
                entertainmentNews.postValue(Resource.Error("No Internet Connection", null))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> entertainmentNews.postValue(Resource.Error("NetworkFailure", null))
                else -> entertainmentNews.postValue(Resource.Error("Conversion Error", null))
            }

        }

    }


    private suspend fun safeSearchNewsCall(searchQuery: String) {
        searchNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepository.searchNews(searchQuery, searchNewsPage)
                searchNews.postValue(handleSearchNewsResponse(response))

            } else {
                searchNews.postValue(Resource.Error("No Internet Connection", null))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchNews.postValue(Resource.Error("Network Failure", null))
                else -> searchNews.postValue(Resource.Error("Conversion Error", null))
            }

        }

    }

    private fun hasInternetConnection(): Boolean {

        val conectivityManager = getApplication<HabariApplication>().getSystemService(
                Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetWork = conectivityManager.activeNetwork ?: return false
            val capabilities = conectivityManager.getNetworkCapabilities(activeNetWork)
                    ?: return false

            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }


        } else {

            conectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }

        }




        return false
    }
}