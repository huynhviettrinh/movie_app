package com.example.appmovieandroid.models.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appmovieandroid.models.Movie
import com.example.appmovieandroid.models.MovieCategory

class FragmentViewModel : ViewModel() {
    val relatedSeries = MutableLiveData<List<Movie>>()
    val recommend = MutableLiveData<List<Movie>>()
    val listMovieCategory = MutableLiveData<List<MovieCategory>>()
    val listBanner = MutableLiveData<List<Movie>>()


    fun setMovieRelatedSeries(newData: List<Movie>) {
        relatedSeries.value = newData
    }

    fun setMovieRecommend(newData: List<Movie>) {
        recommend.value = newData
    }

    fun setMovieCategory(newData: List<MovieCategory>) {
        listMovieCategory.value = newData
    }

    fun setMovieBanner(newData: List<Movie>) {
        listBanner.value = newData
    }


}