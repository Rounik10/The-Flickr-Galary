package com.example.theflickrgalary.model

data class Photo (
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: Int,
    val title: String,
    val ispublic: Int,
    val isfriend: Int,
    val isfamily: Int,
    val url_s: String,
    val height_s:Int,
    val width_s:Int
)