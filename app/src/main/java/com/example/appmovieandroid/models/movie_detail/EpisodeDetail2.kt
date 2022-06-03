package com.example.appmovieandroid.models.movie_detail

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class EpisodeDetail2(
    @SerializedName("episodeId")
    val episodeId: Int,
    @SerializedName("seriesNo")
    val seriesNo: Int,
    @SerializedName("subtitles")
    val subtitles: List<Subtitle>
): Parcelable