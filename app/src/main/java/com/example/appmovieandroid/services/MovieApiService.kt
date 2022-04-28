package com.example.appmovieandroid.services

import com.example.appmovieandroid.util.Const
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieApiService {
    companion object{
        private const val BASE_URL = Const.HOST_LINK
        private var retrofit : Retrofit? = null

        fun getInstance(): Retrofit {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!
        }
    }
}
