package com.example.appmovieandroid.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieCategory(
    @SerializedName("homeSectionName")
    val titleCategory:String,
    @SerializedName("listMovie")
    val listMovie: List<Movie>
) : Parcelable

