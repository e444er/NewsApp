package com.example.newsapp.repo

import com.example.newsapp.data.api.NewsService
import javax.inject.Inject

class Repo @Inject constructor(
    private val newsService: NewsService
) {
    suspend fun getAll() = newsService.getHeadLines()
}