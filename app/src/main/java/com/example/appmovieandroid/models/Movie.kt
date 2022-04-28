package com.example.appmovieandroid.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    @SerializedName("categoryId")
    val categoryId: Int,
    @SerializedName("movieId")
    val movieId: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("mainImage")
    val mainImage: String,
) : Parcelable