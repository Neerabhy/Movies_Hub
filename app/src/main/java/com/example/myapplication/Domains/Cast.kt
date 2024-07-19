package com.example.myapplication.Domains

import java.io.Serializable

data class Cast(
    var PicUrl: String = "", // Use var for mutable properties
    var Actor: String = ""
) : Serializable