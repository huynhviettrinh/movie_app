package com.example.appmovieandroid.models.movie_response

import android.os.Parcelable
import com.example.appmovieandroid.models.MovieCategory
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieCategoryResponse(
    @SerializedName("data")
    val movie : List<MovieCategory>
) : Parcelable

