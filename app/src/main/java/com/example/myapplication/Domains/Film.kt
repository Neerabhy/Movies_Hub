package com.example.myapplication.Domains

import android.provider.MediaStore.Audio.Genres
import android.util.EventLogTags.Description
import java.io.Serializable

data class Film(
    var Title: String = "",
    var Description: String = "",
    var Poster: String = "",
    val Time: String = "",
    val Trailer: String = "",
    val Imdb: Int = 0,
    val Year: Int = 0,
    val Genre: ArrayList<String> = ArrayList(),
    val Casts: ArrayList<Cast> = ArrayList()
) : Serializable
