package com.example.habariapp.ui

import androidx.lifecycle.ViewModel
import com.example.habariapp.repository.NewsRepository

class NewsViewModel(
    val newsRepository:NewsRepository
):ViewModel() {
}