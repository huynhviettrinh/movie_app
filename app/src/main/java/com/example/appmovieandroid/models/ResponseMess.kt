package com.example.appmovieandroid.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResponseMess(
    @SerializedName("status")
    val status : Int,
    @SerializedName("message")
    val mess: String
) : Parcelable
