package com.example.appmovieandroid.models.movie_detail

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Resolution(
    @SerializedName("code")
    val code: String,
    @SerializedName("description")
    val description: String
): Parcelable