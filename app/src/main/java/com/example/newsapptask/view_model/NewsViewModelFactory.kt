package com.example.newsapptask.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsapptask.NewsApplication
import com.example.newsapptask.repo.NewsRepository

class NewsViewModelFactory(
    private val newsRepository: NewsRepository,
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsViewModel(newsRepository = newsRepository, application = application) as T
    }
}