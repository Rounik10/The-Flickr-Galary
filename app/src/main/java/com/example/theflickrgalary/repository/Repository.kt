package com.example.theflickrgalary.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.theflickrgalary.api.FlickrPagingSource
import com.example.theflickrgalary.api.RetrofitInstance
import com.example.theflickrgalary.model.ApiModel

class Repository {
    suspend fun getApiModel(): ApiModel {
        return RetrofitInstance.api.getApiModel(1)
    }

    fun getSearchResPaging(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = {
                FlickrPagingSource(RetrofitInstance.api, query)
            }
        ).liveData
}