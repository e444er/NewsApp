package com.example.newsapp.repo

import com.example.newsapp.data.api.NewsService
import com.example.newsapp.data.db.ArticleDao
import com.example.newsapp.model.Article
import javax.inject.Inject

class Repo @Inject constructor(
    private val newsService: NewsService,
    private val articleDao: ArticleDao
) {
    suspend fun getNews(countryCode: String, pageNumber: Int) =
        newsService.getHeadLines(countryCode, pageNumber)

    suspend fun searchNews(query: String, pageNumber: Int) =
        newsService.getEverything(query, pageNumber)

    fun getFavoriteArticles() = articleDao.getAllArticles()
    suspend fun addArticle(article: Article) = articleDao.insert(article)
    suspend fun deleteArticle(article: Article) = articleDao.delete(article)
}