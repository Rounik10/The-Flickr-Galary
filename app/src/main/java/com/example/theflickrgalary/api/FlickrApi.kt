package com.example.theflickrgalary.api

import com.example.theflickrgalary.model.ApiModel
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApi {

    /*
        https://api.flickr.com/services/rest/
        ?method=flickr.photos.getRecent&per_page=20
        &page=1
        &api_key=6f102c62f41998d151e5a1b48713cf13
        &format=json
        &nojsoncallback=1
        &extras=url_s
    */

    @GET("?method=flickr.photos.getRecent&per_page=20&api_key=6f102c62f41998d151e5a1b48713cf13&format=json&nojsoncallback=1&extras=url_s")
    suspend fun getApiModel(@Query("page") page: Int) : ApiModel

    @GET("?method=flickr.photos.search&api_key=6f102c62f41998d151e5a1b48713cf13&format=json&nojsoncallback=1&extras=url_s")
    suspend fun searchImages(
        @Query("text") query: String,
        @Query("page") page: Int
    ) : ApiModel

}