package com.example.movieapp.network

import com.example.movieapp.data.MovieRoot
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface IMovieAPIService {
    @GET("v1/search/movie.json")
    fun getMoviesByTitle (
        @Header("X-Naver-Client-Id") clientId: String,
        @Header("X-Naver-Client-Secret") clientSecret: String,
        @Query("query") keyword: String,
    )  : Call<MovieRoot>

}