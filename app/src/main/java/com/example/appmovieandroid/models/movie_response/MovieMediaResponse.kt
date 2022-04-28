package com.example.appmovieandroid.models.movie_response

import android.os.Parcelable
import com.example.appmovieandroid.models.MovieMedia
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class MovieMediaResponse (
    @SerializedName("data")
    val movieMedia : MovieMedia
) : Parcelable