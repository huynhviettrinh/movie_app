package com.example.appmovieandroid.common

import android.util.Log
import com.example.appmovieandroid.models.Movie
import com.example.appmovieandroid.models.post_data_server.PostMovieFavorite
import com.example.appmovieandroid.models.ResponseMess
import com.example.appmovieandroid.models.post_data_server.PostStringUid
import com.example.appmovieandroid.models.movie_response.MovieModelResponse
import com.example.appmovieandroid.services.MoreFeature
import com.example.appmovieandroid.services.MovieApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoreFeature {

    companion object {
        lateinit var body: PostMovieFavorite
        var uid: String = ""
        var isHandleStoreListFavorite = false

        fun addMovieIntoListFavorite(callBack: (ResponseMess) -> Unit) {
            val apiService = MovieApiService.getInstance().create(MoreFeature::class.java)
            val apiServiceRes = apiService.addMovieIntoFavorite(body)
            apiServiceRes.enqueue(object : Callback<ResponseMess> {
                override fun onResponse(
                    call: Call<ResponseMess>,
                    response: Response<ResponseMess>
                ) {
                    return callBack(response.body()!!)
                }

                override fun onFailure(call: Call<ResponseMess>, t: Throwable) {
                    Log.e("Failure", "getMovieSearchWithKeyWord:" + t.message)
                }

            })
        }

        fun listMovieFavoriteByUid(callBack: (List<Movie>) -> Unit) {
            val apiService = MovieApiService.getInstance().create(MoreFeature::class.java)
            val apiServiceRes =
                apiService.listMovieFavoriteByUid(PostStringUid(uid))
            apiServiceRes.enqueue(object : Callback<MovieModelResponse> {
                override fun onResponse(
                    call: Call<MovieModelResponse>,
                    response: Response<MovieModelResponse>
                ) {
                    return callBack(response.body()!!.movie)
                }

                override fun onFailure(call: Call<MovieModelResponse>, t: Throwable) {
                    Log.e("Failure", "getMovieSearchWithKeyWord:" + t.message)
                }
            })
        }
    }


}