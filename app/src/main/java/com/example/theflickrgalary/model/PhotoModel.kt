package com.example.theflickrgalary.model

data class PhotoModel(
    val page:Int,
    val pages:Int,
    val prepage:Int,
    val total:Int,
    val photo:List<Photo>
)