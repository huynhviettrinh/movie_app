package com.example.appmovieandroid.models.view_model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appmovieandroid.models.Movie


class ConstViewModel : ViewModel() {
    val uid = MutableLiveData<String>()
    val listMovieFavorite = MutableLiveData<List<Movie>>()

    fun setUidUser(newData: String) {
        uid.value = newData
    }

    fun setMovieFavorite(newData: List<Movie>) {
        listMovieFavorite.value = newData
    }
}