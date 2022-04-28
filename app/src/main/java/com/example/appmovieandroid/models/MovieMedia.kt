package com.example.appmovieandroid.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieMedia(
    @SerializedName("episodeId")
    val episodeId: String,
    @SerializedName("mediaUrl")
    val mediaUrl: String,
    @SerializedName("totalDuration")
    val totalDuration: Int
) : Parcelable
