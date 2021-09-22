package com.example.newsapptask.view_model

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapptask.NewsApplication
import com.example.newsapptask.models.NewsResponse
import com.example.newsapptask.repo.NewsRepository
import com.example.newsapptask.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(val newsRepository: NewsRepository,  application: Application) :
    AndroidViewModel(application) {

    val topHeadlinesResponse: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val searchNewsResponse: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()

    init {
        getTopHeadlinesNews()
    }

    fun getTopHeadlinesNews() = viewModelScope.launch {
        topHeadlinesResponse.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response: Response<NewsResponse> = newsRepository.getTopHeadlinesNews()
                topHeadlinesResponse.postValue(monitorTopHeadlinesResponse(response))
            } else {
                topHeadlinesResponse.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            topHeadlinesResponse.postValue(Resource.Error("${t.message}"))
        }
    }

    private fun monitorTopHeadlinesResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { newsResponse ->
                return Resource.Success(newsResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        searchNewsResponse.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response: Response<NewsResponse> = newsRepository.searchNews(searchQuery)
                searchNewsResponse.postValue(monitorSearchNewsResponse(response))
            } else {
                searchNewsResponse.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            searchNewsResponse.postValue(Resource.Error("${t.message}"))
        }
    }

    private fun monitorSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

}