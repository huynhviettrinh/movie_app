package com.example.appmovieandroid.models.movie_detail

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class EpisodeDetail(
    @SerializedName("episodeId")
    val episodeId: Int,
    @SerializedName("resolution")
    val resolution: List<Resolution>,
    @SerializedName("seriesNo")
    val seriesNo: Int,
    @SerializedName("subtitles")
    val subtitles: List<Subtitle>
): Parcelable