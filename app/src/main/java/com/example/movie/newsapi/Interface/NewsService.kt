package com.example.movie.newsapi.Interface

import com.example.movie.newsapi.Model.News
import com.example.movie.newsapi.Model.Website
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface NewsService {

    //there is no need of https://newsapi.org/ because this is the BASE_URL that has been declared on Common

    @get:GET( "v2/sources?apiKey=41dfadaa1d55494d85ae31b3834b28a6")
    val sources:Call<Website>

    @GET
    fun getNewsFromSource(@Url url:String):Call<News>
}