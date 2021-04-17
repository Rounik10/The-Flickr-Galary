package com.example.theflickrgalary.api

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.theflickrgalary.model.Photo

class FlickrPagingSource(
    private val flickrApi: FlickrApi,
    private val query: String
) : PagingSource<Int, Photo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        val position = params.key ?: 1

        return try {
            val response =
                if (query == "recent") flickrApi.getApiModel(position)
                else flickrApi.searchImages(query, position)
            val photos = response.photos.photo

            LoadResult.Page(
                data = photos,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (photos.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        return state.anchorPosition
    }
}