package com.example.movie.newsapi.Common

import com.example.movie.newsapi.Interface.NewsService
import com.example.movie.newsapi.Remote.RetrofitClient
import java.lang.StringBuilder
import java.nio.channels.spi.AbstractSelectionKey

object Common
{
    val BASE_URL = "https://newsapi.org/"
    val API_KEY = ""

    val newsService:NewsService
        get()=RetrofitClient.getClient(BASE_URL).create(NewsService::class.java)


    //get news function
    fun getNewsApi(source:String):String{
        val apiUrl = StringBuilder("https://newsapi.org/v2/top-headlines?sources=").append(source).append("&apiKey=").append(
            API_KEY).toString()
        return apiUrl
    }
}