package com.example.appmovieandroid.services

import com.example.appmovieandroid.models.post_data_server.PostMovieFavorite
import com.example.appmovieandroid.models.ResponseMess
import com.example.appmovieandroid.models.post_data_server.PostStringUid
import com.example.appmovieandroid.models.movie_response.MovieModelResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MoreFeature {
    @POST("/addMovieFavorite")
    fun addMovieIntoFavorite(@Body body: PostMovieFavorite):Call<ResponseMess>

    @POST("/listMovieFavorite")
    fun listMovieFavoriteByUid(@Body uid: PostStringUid):Call<MovieModelResponse>

}