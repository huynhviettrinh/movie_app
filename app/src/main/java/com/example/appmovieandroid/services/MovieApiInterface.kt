package com.example.appmovieandroid.services

import com.example.appmovieandroid.models.movie_response.MovieModelResponse
import com.example.appmovieandroid.models.movie_response.MovieCategoryResponse
import com.example.appmovieandroid.models.movie_response.MovieDetailResponse
import com.example.appmovieandroid.models.movie_response.MovieMediaResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiInterface {
    @GET("/getListMovie?")
    fun getMovieList(@Query("page") page: Int): Call<MovieCategoryResponse>

    @GET("/getBanner")
    fun getBannerMovie(): Call<MovieModelResponse>

    @GET("/getDetailMovie?")
    fun getMovieDetail(
        @Query("id") id: Int,
        @Query("category") category: Int
    ): Call<MovieDetailResponse>


    @GET("/getMovieMedia?")
    fun getMovieMedia(
        @Query("category") category: Int,
        @Query("contentId") contentId: Int,
        @Query("episodeId") episodeId: Int,
        @Query("definition") definition: String,
        ): Call<MovieMediaResponse>

    @GET("/apiSearchWithKeyWord?")
    fun getMovieSearchWithKeyWord(
        @Query("searchKeyWord") searchKeyWord: String,
    ): Call<MovieModelResponse>

}