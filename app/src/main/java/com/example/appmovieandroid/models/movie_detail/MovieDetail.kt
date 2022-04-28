package com.example.appmovieandroid.models.movie_detail

import android.os.Parcelable
import com.example.appmovieandroid.models.Movie
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class MovieDetail(
    @SerializedName("movieId")
    val movieId: Int,

    @SerializedName("categoryId")
    val categoryId: Int,

    @SerializedName("releaseYear")
    val releaseYear: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("trailerId")
    val trailerId: String,

    @SerializedName("score")
    val score: Double,

    @SerializedName("category")
    val category: List<String>,

    @SerializedName("episodeCount")
    val episodeCount: Int,

    @SerializedName("nation")
    val nation: String,

    @SerializedName("introduction")
    val introduction: String,

    @SerializedName("mainImage")
    val mainImage: String,

    @SerializedName("bannerImage")
    val bannerImage: String,

    @SerializedName("recommendMovies")
    val recommendMovies: List<Movie>,

    @SerializedName("relatedSeries")
    val relatedSeries: List<Movie>,

    @SerializedName("episodeDetails")
    val episodeDetails: List<EpisodeDetail>,

    ) : Serializable, Parcelable
