package com.example.appmovieandroid.common

import android.util.Log
import com.example.appmovieandroid.models.*
import com.example.appmovieandroid.models.movie_detail.MovieDetail
import com.example.appmovieandroid.models.movie_detail.MovieDetail2
import com.example.appmovieandroid.models.movie_response.*
import com.example.appmovieandroid.services.MovieApiInterface
import com.example.appmovieandroid.services.MovieApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CompanionObject {
    companion object {

        var isCallApi = false
        var movieId: Int = 0
        var categoryId: Int = 0
        var episodeId: Int = 0
        var definition: String = ""
        var keyword = ""
        var isWelcome = false


        // TODO get list movie by page
        fun getMovieData(callBack: (List<MovieCategory>) -> Unit) {
            val apiService = MovieApiService.getInstance().create(MovieApiInterface::class.java)
            apiService.getMovieList(0).enqueue(object : Callback<MovieCategoryResponse> {
                override fun onResponse(
                    call: Call<MovieCategoryResponse>,
                    response: Response<MovieCategoryResponse>
                ) {
                    return callBack(response.body()!!.movie)
                }

                override fun onFailure(call: Call<MovieCategoryResponse>, t: Throwable) {
                    Log.e("Failure", "getMovieData:" + t.message)
                }
            })
        }

        // TODO get movie banner
        fun getMovieBanner(callBack: (List<Movie>) -> Unit) {
            val apiService = MovieApiService.getInstance().create(MovieApiInterface::class.java)
            apiService.getBannerMovie().enqueue(object : Callback<MovieModelResponse> {
                override fun onResponse(
                    call: Call<MovieModelResponse>,
                    response: Response<MovieModelResponse>
                ) {
                    return callBack(response.body()!!.movie)
                }

                override fun onFailure(call: Call<MovieModelResponse>, t: Throwable) {
                    Log.e("Failure", "getMovieBanner:" + t.message)
                }
            })
        }


        fun getMovieDetail(callBack: (MovieDetail) -> Unit) {
            try {
                val apiService = MovieApiService.getInstance().create(MovieApiInterface::class.java)
                apiService.getMovieDetail(movieId, categoryId)
                    .enqueue(object : Callback<MovieDetailResponse> {
                        override fun onResponse(
                            call: Call<MovieDetailResponse>,
                            response: Response<MovieDetailResponse>
                        ) {
                            return callBack(response.body()!!.movieDetail)
                        }

                        override fun onFailure(call: Call<MovieDetailResponse>, t: Throwable) {
                            Log.e("Failure", "getMovieDetail:" + t.message)
                        }
                    })
            } catch (e: Exception) {
                Log.v("getMovieDetail", e.toString())

            }
        }

        fun getMovieMedia(callBack: (MovieMedia) -> Unit) {
            val apiService = MovieApiService.getInstance().create(MovieApiInterface::class.java)
            apiService.getMovieMedia(categoryId, movieId, episodeId, definition)
                .enqueue(object : Callback<MovieMediaResponse> {
                    override fun onResponse(
                        call: Call<MovieMediaResponse>,
                        response: Response<MovieMediaResponse>
                    ) {
                        return callBack(response.body()!!.movieMedia)
                    }
                    override fun onFailure(call: Call<MovieMediaResponse>, t: Throwable) {
                        Log.e("Failure", "getMovieMedia:" + t.message)
                    }
                })
        }

        // TODO get movie search
        fun getMovieSearchWithKeyWord(callBack: (List<Movie>) -> Unit) {
            val apiService = MovieApiService.getInstance().create(MovieApiInterface::class.java)
            apiService.getMovieSearchWithKeyWord(keyword)
                .enqueue(object : Callback<MovieModelResponse> {
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

        //////////////////////////////////////////////////////////////////////////////////////////


        fun getListMovie(callBack: (List<MovieCategory>) -> Unit) {
            val apiService = MovieApiService.getInstance().create(MovieApiInterface::class.java)
            apiService.getListMovie().enqueue(object : Callback<MovieCategoryResponse> {
                override fun onResponse(
                    call: Call<MovieCategoryResponse>,
                    response: Response<MovieCategoryResponse>
                ) {
                    return callBack(response.body()!!.movie)
                }

                override fun onFailure(call: Call<MovieCategoryResponse>, t: Throwable) {
                    Log.e("Failure", "getListMovie:" + t.message)
                }
            })
        }

        fun getDetailMovie(callBack: (MovieDetail2) -> Unit) {
            try {
                val apiService = MovieApiService.getInstance().create(MovieApiInterface::class.java)
                apiService.getDetailMovie(movieId, categoryId)
                    .enqueue(object : Callback<MovieDetailResponse2> {
                        override fun onResponse(
                            call: Call<MovieDetailResponse2>,
                            response: Response<MovieDetailResponse2>
                        ) {
                            return callBack(response.body()!!.movieDetail)
                        }

                        override fun onFailure(call: Call<MovieDetailResponse2>, t: Throwable) {
                            Log.e("Failure", "getDetailMovie:" + t.message)
                        }
                    })
            } catch (e: Exception) {
                Log.v("getDetailMovie", e.toString())

            }
        }

        fun getMediaMovie(callBack: (MovieMedia) -> Unit) {
            val apiService = MovieApiService.getInstance().create(MovieApiInterface::class.java)
                apiService.getMediaMovie(categoryId, movieId, episodeId)
                .enqueue(object : Callback<MovieMediaResponse> {
                    override fun onResponse(
                        call: Call<MovieMediaResponse>,
                        response: Response<MovieMediaResponse>
                    ) {
                        return callBack(response.body()!!.movieMedia)
                    }
                    override fun onFailure(call: Call<MovieMediaResponse>, t: Throwable) {
                        Log.e("Failure", "getMediaMovie:" + t.message)
                    }
                })
        }

    }
}