package com.example.appmovieandroid.models.post_data_server

data class PostMovieFavorite(
    val userId: String,
    val movieId: Int,
    val categoryId: Int,
    val mainImage: String,
    val name: String,
    val checkExistList: Boolean,
)