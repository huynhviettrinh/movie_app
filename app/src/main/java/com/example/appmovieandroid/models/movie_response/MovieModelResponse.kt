package com.example.appmovieandroid.models.movie_response

import android.os.Parcelable
import com.example.appmovieandroid.models.Movie
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieModelResponse(
    @SerializedName("data")
    val movie : List<Movie>
) : Parcelable