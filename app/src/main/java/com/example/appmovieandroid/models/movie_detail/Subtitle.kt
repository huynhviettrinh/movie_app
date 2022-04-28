package com.example.appmovieandroid.models.movie_detail

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Subtitle(
    @SerializedName("language")
    val language: String,
    @SerializedName("languageAbbr")
    val languageAbbr: String,
    @SerializedName("subtitlingUrl")
    val subtitlingUrl: String
) : Parcelable