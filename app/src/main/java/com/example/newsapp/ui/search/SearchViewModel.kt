package com.example.newsapp.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.model.NewsResponse
import com.example.newsapp.repo.Repo
import com.example.newsapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repo: Repo
) : ViewModel() {

    val searchLiveData: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchPage = 1

    fun getSearchNews(query: String) =
        viewModelScope.launch {
            searchLiveData.postValue(Resource.Loading())
            val response = repo.searchNews(query , pageNumber = searchPage)
            if (response.isSuccessful) {
                response.body().let { res ->
                    searchLiveData.postValue(Resource.Success(res))
                }
            } else {
                searchLiveData.postValue(Resource.Error(message = response.message()))
            }
        }
}