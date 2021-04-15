package com.example.theflickrgalary.repository

import com.example.theflickrgalary.api.RetrofitInstance
import com.example.theflickrgalary.model.ApiModel

class Repository {
    suspend fun getApiModel() : ApiModel {
        return RetrofitInstance.api.getApiModel()
    }

    suspend fun getSearchResult(tags:String) : ApiModel {
        return RetrofitInstance.api.searchImages(tags)
    }
}