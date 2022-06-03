package com.example.appmovieandroid.models.movie_response

import android.os.Parcelable
import com.example.appmovieandroid.models.movie_detail.MovieDetail2
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieDetailResponse2(
    @SerializedName("data")
    val movieDetail : MovieDetail2
) : Parcelable
