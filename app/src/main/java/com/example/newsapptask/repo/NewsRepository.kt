package com.example.newsapptask.repo
import com.example.newsapptask.rest_api.RetrofitInstance.Companion.api

class NewsRepository {
    suspend fun getTopHeadlinesNews() = api.getTopHeadlinesNews()

    suspend fun searchNews(searchQuery: String) =
        api.searchForNews(searchQuery)
}