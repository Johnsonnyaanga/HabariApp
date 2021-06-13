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

class NewsViewModel(
        val newsRepository: NewsRepository,application: Application
) : AndroidViewModel(application) {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    var breakingNewsResponse: NewsResponse? = null

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null


    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())
        val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
        breakingNews.postValue(handleBreakingNewsResponse(response))
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        val response = newsRepository.searchNews(searchQuery, searchNewsPage)
        searchNews.postValue(handleSearchNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                breakingNewsPage++
                if(breakingNewsResponse == null) {
                    breakingNewsResponse = resultResponse
                } else {
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message(),null)
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchNewsPage++
                if(searchNewsResponse == null) {
                    searchNewsResponse = resultResponse
                } else {
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message(),null)
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.insertArticle(article)
    }

    fun getSavedNews() = newsRepository.getAllArticles()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }
    private fun hasInternetConnection():Boolean{

val conectivityManager = getApplication<HabariApplication>().getSystemService(
        Context.CONNECTIVITY_SERVICE
) as ConnectivityManager
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            val activeNetWork = conectivityManager.activeNetwork ?: return false
            val capabilities =  conectivityManager.getNetworkCapabilities(activeNetWork) ?: return false

            return when{
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                    capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                    capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }



        }else{

            conectivityManager.activeNetworkInfo?.run {
                return when(type){
                    TYPE_WIFI ->  true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }

        }




       return false
    }
}