package com.example.theflickrgalary.api

import com.example.theflickrgalary.util.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: FlickrApi by lazy {
        retrofit.create(FlickrApi::class.java)
    }

}