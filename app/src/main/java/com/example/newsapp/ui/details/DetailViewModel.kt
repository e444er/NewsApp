package com.example.newsapp.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.model.Article
import com.example.newsapp.model.NewsResponse
import com.example.newsapp.repo.Repo
import com.example.newsapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repo: Repo
) : ViewModel() {

    val getArticles: LiveData<List<Article>> =
        repo.getFavoriteArticles()

//    fun getArticles() = viewModelScope.launch(Dispatchers.IO) {
//         repo.getFavoriteArticles()
//    }

    fun saveArticles(article: Article) = viewModelScope.launch(Dispatchers.IO) {
        repo.addArticle(article)
    }

    fun deleteArticles(article: Article) = viewModelScope.launch(Dispatchers.IO) {
        repo.deleteArticle(article)
    }
}